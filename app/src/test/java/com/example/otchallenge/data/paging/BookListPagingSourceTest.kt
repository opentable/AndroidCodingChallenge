package com.example.otchallenge.data.paging

import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.testing.TestPager
import com.example.otchallenge.RemoteServerExceptionFactory
import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.data.paging.booklist.BookListPagingSourceFactory
import com.example.otchallenge.data.remote.BooksApi
import com.example.otchallenge.data.remote.RemoteRequestError
import com.example.otchallenge.data.remote.responses.BookItemResponse
import com.example.otchallenge.data.remote.responses.BookListResponse
import com.example.otchallenge.data.remote.responses.BookListResultResponse
import com.example.otchallenge.di.PagingSourceTestComponent
import com.example.otchallenge.di.data.PagingConfigModule
import io.mockk.every
import io.reactivex.Single
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.amshove.kluent.shouldBeNull
import org.amshove.kluent.shouldHaveSize
import org.amshove.kluent.shouldNotBeNull
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

class BookListPagingSourceTest {

    @Inject
    lateinit var pagingSourceFactory: BookListPagingSourceFactory<Int, Book>

    @Inject
    @Named(PagingConfigModule.BookList)
    lateinit var pagingConfig: PagingConfig

    @Inject
    lateinit var booksApi: BooksApi

    @Inject
    lateinit var remoteServerExceptionFactory: RemoteServerExceptionFactory

    private lateinit var pager: TestPager<Int, Book>

    @Before
    fun setUp() {
        PagingSourceTestComponent.instance.inject(this)
        pager = TestPager(
            config = pagingConfig,
            pagingSource = pagingSourceFactory("")
        )
    }

    private fun buildPage(size: Int = 20): List<BookItemResponse> {
        return List(size) { i ->
            BookItemResponse(
                title = "title${i+1}",
                author = "author${i+1}",
                description = "description${i+1}",
                bookImage = "image${i+1}"
            )
        }
    }

    private fun buildResponse(pageSize: Int = 20): BookListResponse {
        return BookListResponse(
            numberOfResults = pageSize,
            results = BookListResultResponse(
                listName = "test",
                books = buildPage(pageSize)
            )
        )
    }

    private fun PagingSource.LoadResult<Int, Book>?.assertPage(
        pageSize: Int,
    ): PagingSource.LoadResult.Page<Int, Book> {
        shouldNotBeNull()
        with (shouldBeInstanceOf<PagingSource.LoadResult.Page<Int, Book>>()) {
            data.firstOrNull().shouldNotBeNull().title shouldBeEqualTo "title1"
            data.lastOrNull().shouldNotBeNull().title shouldBeEqualTo "title${pageSize}"
            data.shouldHaveSize(pageSize)
            return this
        }
    }

    @Test
    fun `Initial load successful and prevKey is null`() = runTest {

        every { booksApi.getBookList(name = any(), date = any(), offset = 0) } returns
            Single.just(buildResponse(pageSize = pagingConfig.initialLoadSize))

        val result = pager.refresh()

        with (result.assertPage(pagingConfig.initialLoadSize)) {
            prevKey.shouldBeNull()
            nextKey.shouldNotBeNull().shouldBeEqualTo(pagingConfig.initialLoadSize)
        }

    }

    @Test
    fun `After initial load nextKey should be null if fetched page size is less than initialLoadSize`() = runTest {

        val fetchedPageSize = pagingConfig.initialLoadSize - 1

        every { booksApi.getBookList(name = any(), date = any(), offset = 0) } returns
                Single.just(buildResponse(pageSize = fetchedPageSize))

        val result = pager.refresh()

        with(result.assertPage(fetchedPageSize)) {
            prevKey.shouldBeNull()
            nextKey.shouldBeNull()
        }
    }

    @Test
    fun `Appends next pages succesfully`() = runTest {
        every {
            booksApi.getBookList(
                name = any(),
                date = any(),
                offset = 0
            )
        } returns Single.just(buildResponse(pageSize = pagingConfig.initialLoadSize))

        every {
            booksApi.getBookList(
                name = any(),
                date = any(),
                offset = pagingConfig.initialLoadSize
            )
        } returns Single.just(buildResponse(pageSize = pagingConfig.pageSize))

        every {
            booksApi.getBookList(
                name = any(),
                date = any(),
                offset = pagingConfig.initialLoadSize + pagingConfig.pageSize
            )
        } returns Single.just(buildResponse(pageSize = pagingConfig.pageSize - 1))

        pager.refresh()
        pager.append()
        pager.append()

        val pages = pager.getPages()

        pages.shouldHaveSize(3)
        with (pages[0].assertPage(pagingConfig.initialLoadSize)) {
            prevKey.shouldBeNull()
            nextKey.shouldBeEqualTo(pagingConfig.initialLoadSize)
        }
        with (pages[1].assertPage(pagingConfig.pageSize)) {
            prevKey.shouldBeEqualTo(0)
            nextKey.shouldBeEqualTo(pagingConfig.pageSize + pagingConfig.initialLoadSize)
        }
        with(pages[2].assertPage(pagingConfig.pageSize-1)) {
            prevKey.shouldBeEqualTo(pagingConfig.initialLoadSize)
            nextKey.shouldBeNull()
        }

    }

    @Test
    fun `Appends and prepends pages succesfully`() = runTest {
        every {
            booksApi.getBookList(
                name = any(),
                date = any(),
                offset = 0
            )
        } returns Single.just(buildResponse(pageSize = pagingConfig.initialLoadSize))

        every {
            booksApi.getBookList(
                name = any(),
                date = any(),
                offset = pagingConfig.initialLoadSize
            )
        } returns Single.just(buildResponse(pageSize = pagingConfig.pageSize))

        every {
            booksApi.getBookList(
                name = any(),
                date = any(),
                offset = pagingConfig.initialLoadSize + pagingConfig.pageSize
            )
        } returns Single.just(buildResponse(pageSize = pagingConfig.pageSize - 1))

        pager.refresh(pagingConfig.initialLoadSize)
        pager.prepend()
        pager.append()

        val pages = pager.getPages()

        pages.shouldHaveSize(3)
        with (pages[0].assertPage(pagingConfig.initialLoadSize)) {
            prevKey.shouldBeNull()
            nextKey.shouldBeEqualTo(pagingConfig.initialLoadSize)
        }
        with (pages[1].assertPage(pagingConfig.pageSize)) {
            prevKey.shouldBeEqualTo(0)
            nextKey.shouldBeEqualTo(pagingConfig.pageSize + pagingConfig.initialLoadSize)
        }
        with(pages[2].assertPage(pagingConfig.pageSize-1)) {
            prevKey.shouldBeEqualTo(pagingConfig.initialLoadSize)
            nextKey.shouldBeNull()
        }

    }

    @Test
    fun `Check connection timeout error`() = runTest {

        every {
            booksApi.getBookList(
                name = any(),
                date = any(),
                offset = 0
            )
        } returns Single.error(SocketTimeoutException())

        val result = pager.refresh()

        with (result.shouldBeInstanceOf<PagingSource.LoadResult.Error<Int, Book>>()) {
            throwable.shouldBeInstanceOf<RemoteRequestError.ConnectionTimeout>()
        }

    }

    @Test
    fun `Check no connection error`() = runTest {

        every {
            booksApi.getBookList(
                name = any(),
                date = any(),
                offset = 0
            )
        } returns Single.error(IOException())

        val result = pager.refresh()

        with (result.shouldBeInstanceOf<PagingSource.LoadResult.Error<Int, Book>>()) {
            throwable.shouldBeInstanceOf<RemoteRequestError.NoConnection>()
        }

    }

    @Test
    fun `Check server error`() = runTest {

        every {
            booksApi.getBookList(
                name = any(),
                date = any(),
                offset = 0
            )
        } returns Single.error(remoteServerExceptionFactory(code = 500))

        val result = pager.refresh()

        with (result.shouldBeInstanceOf<PagingSource.LoadResult.Error<Int, Book>>()) {
            throwable.shouldBeInstanceOf<RemoteRequestError.Server>()
                .code.shouldBeEqualTo(500)
        }

    }
}