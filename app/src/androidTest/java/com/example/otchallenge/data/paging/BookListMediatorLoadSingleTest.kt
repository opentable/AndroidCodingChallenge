package com.example.otchallenge.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.test.platform.app.InstrumentationRegistry
import com.example.otchallenge.data.local.AppDatabase
import com.example.otchallenge.data.local.dao.BookListEntryBooksDao
import com.example.otchallenge.data.local.dao.BooksDao
import com.example.otchallenge.data.local.entities.BookEntity
import com.example.otchallenge.data.local.entities.BookListEntity
import com.example.otchallenge.data.local.entities.BookListEntryBooksEntity
import com.example.otchallenge.data.local.entities.BookListEntryEntity
import com.example.otchallenge.data.local.entities.BookListRemoteKeyEntity
import com.example.otchallenge.data.paging.booklist.BookListMediator
import com.example.otchallenge.data.remote.BooksApi
import com.example.otchallenge.di.DaggerPagingSourceTestComponent
import com.example.otchallenge.di.data.PagingConfigModule
import io.mockk.every
import io.reactivex.Single
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeFalse
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeTrue
import org.amshove.kluent.shouldContain
import org.amshove.kluent.shouldContainAll
import org.amshove.kluent.shouldHaveSize
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameter
import org.junit.runners.Parameterized.Parameters
import java.time.Instant
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

@RunWith(Parameterized::class)
class BookListMediatorLoadSingleTest {

    @Inject
    lateinit var booksApi: BooksApi

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var mediator: BookListMediator

    @Inject
    @Named(PagingConfigModule.BookList)
    lateinit var pagingConfig: PagingConfig

    @Parameter(0)
    lateinit var options: BookListMediator.Options


    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        DaggerPagingSourceTestComponent.builder()
            .applicationContext(appContext)
            .build()
            .inject(this)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun insertsRefreshPageSuccessfully() {

        val response = generateResponse(listId = LIST_ID, books = 10)
        every { booksApi.getBookList(any(), any(), any()) } returns Single.just(response)

        mediator.withOptions(options)

        val observer = mediator.loadSingle(
            LoadType.REFRESH,
            PagingState(
                pages = emptyList(),
                anchorPosition = 0,
                config = pagingConfig,
                10
            )
        ).test()

        observer.assertValueCount(1)
        observer.values()[0]
            .shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
            .endOfPaginationReached.shouldBeTrue()

        with(database.bookListsDao().getAll()) {
            shouldHaveSize(1)
            shouldContain(
                BookListEntity(
                    id = LIST_ID,
                    name = "Test List",
                    lastEntryDate = LocalDate.now()
                )
            )
        }

        with (database.bookListEntriesDao().getAll()) {
            shouldHaveSize(1)
            shouldContain(
                BookListEntryEntity(
                    bookListId = LIST_ID,
                    date = LocalDate.now(),
                    size = 10,
                    lastModified = Instant
                        .ofEpochMilli(
                            response.lastModified.toInstant().epochSecond * 1000
                        )
                )
            )
        }

        with (database.bookListRemoteKeysDao().getAll()) {
            shouldHaveSize(1)
            shouldContain(
                BookListRemoteKeyEntity(
                    bookListId = LIST_ID,
                    date = LocalDate.now(),
                    nextKey = null
                )
            )
        }

        database.booksDao().checkAll(10)
        database.bookListEntryBooksDao().checkAll(10)

    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun storesNextPageSuccessfully() {

        val date = LocalDate.now()
        val apiDate = when(options) {
            is BookListMediator.Options.LoadCurrent -> "current"
            is BookListMediator.Options.LoadDate -> date.toString()
        }

        val refreshResponse = generateResponse(
            listId = LIST_ID,
            books = pagingConfig.pageSize,
            totalCount = 25,
            date = date
        )

        val appendResponse = generateResponse(
            listId = LIST_ID,
            books = 5,
            offset = pagingConfig.pageSize,
            totalCount = 25,
            date = date
        )

        every { booksApi.getBookList(any(), apiDate, offset = 0) } returns Single.just(refreshResponse)
        every { booksApi.getBookList(any(), date.toString(), offset = 20) } returns Single.just(appendResponse)

        mediator.withOptions(options)

        var observer = mediator.loadSingle(
            LoadType.REFRESH,
            PagingState(
                pages = emptyList(),
                anchorPosition = 0,
                config = pagingConfig,
                10
            )
        ).test()

        observer.assertValueCount(1)
        observer.values()[0].shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
            .endOfPaginationReached.shouldBeFalse()
        observer.dispose()

        with (database.bookListRemoteKeysDao().getAll()) {
            shouldHaveSize(1)
            shouldContain(
                BookListRemoteKeyEntity(
                    bookListId = LIST_ID,
                    date = date,
                    nextKey = 20
                )
            )
        }

        observer = mediator.loadSingle(
            LoadType.APPEND,
            PagingState(
                pages = emptyList(),
                anchorPosition = 0,
                config = pagingConfig,
                10
            )
        ).test()


        observer.values()[0].shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
            .endOfPaginationReached.shouldBeTrue()

        with (database.bookListEntriesDao().getAll()) {
            shouldHaveSize(1)
            first().size.shouldBeEqualTo(25)
        }

        with (database.bookListRemoteKeysDao().getAll()) {
            shouldHaveSize(1)
            shouldContain(
                BookListRemoteKeyEntity(
                    bookListId = LIST_ID,
                    date = date,
                    nextKey = null
                )
            )
        }

        database.booksDao().checkAll(25)
        database.bookListEntryBooksDao().checkAll(count = 25, date = date)

    }


    private fun BooksDao.checkAll(count: Int) {
        with (getAll()) {
            shouldHaveSize(count)
            shouldContainAll(
                List(count) {
                    BookEntity(
                        isbn13 = "$it",
                        title = "title$it",
                        author = "author$it",
                        description = "desc$it",
                        imageUrl = "image$it"
                    )
                }
            )
        }
    }

    private fun BookListEntryBooksDao.checkAll(count: Int, date: LocalDate = LocalDate.now()) {
        with (getAll()) {
            shouldHaveSize(count)
            shouldContainAll(
                List(count) {
                    BookListEntryBooksEntity(
                        bookListId = LIST_ID,
                        date = date,
                        bookIsbn13 = "$it",
                        rank = it
                    )
                }
            )
        }
    }

    companion object {

        private const val LIST_ID = "test-list"

        @Parameters
        @JvmStatic
        fun parameters(): Iterable<Array<Any>> {
            return listOf(
                arrayOf(BookListMediator.Options.LoadCurrent(LIST_ID)),
                arrayOf(BookListMediator.Options.LoadDate(LIST_ID, LocalDate.now())),
            )
        }

    }

}