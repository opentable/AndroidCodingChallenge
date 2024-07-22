package com.example.otchallenge.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.test.platform.app.InstrumentationRegistry
import com.example.otchallenge.data.paging.booklist.BookListMediator
import com.example.otchallenge.data.remote.BooksApi
import com.example.otchallenge.data.remote.RemoteRequestError
import com.example.otchallenge.di.DaggerPagingSourceTestComponent
import com.example.otchallenge.di.data.PagingConfigModule
import io.mockk.every
import io.reactivex.Single
import okhttp3.ResponseBody.Companion.toResponseBody
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.junit.runners.Parameterized.Parameters
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named
import kotlin.reflect.KClass

@RunWith(Parameterized::class)
class BookListMediatorLoadSingleErrorTest {

    @Inject
    lateinit var booksApi: BooksApi

    @Inject
    lateinit var mediator: BookListMediator

    @Inject
    @Named(PagingConfigModule.BookList)
    lateinit var pagingConfig: PagingConfig

    @Parameterized.Parameter(0)
    lateinit var klass: KClass<RemoteRequestError>

    @Parameterized.Parameter(1)
    lateinit var error: Throwable

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
    fun returnsRemoteRequestErrorOnRefreshError() {

        every { booksApi.getBookList(any(), any(), any()) } returns Single.error(error)

        mediator.withOptions(BookListMediator.Options.LoadCurrent("test-list"))

        val observer = mediator
            .loadSingle(
                loadType = LoadType.REFRESH,
                state = PagingState(
                    pages = emptyList(),
                    anchorPosition = 0,
                    config = pagingConfig,
                    10
                )
            ).test()

        observer.assertValueCount(1)
        observer.values()[0].shouldBeInstanceOf<RemoteMediator.MediatorResult.Error>()
            .throwable
            .shouldBeInstanceOf<RemoteRequestError>().apply {
                cause.shouldBe(error)
                shouldBeInstanceOf(klass)
            }
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun returnsRemoteRequestErrorOnAppendError() {

        val date = LocalDate.now().toString()

        every { booksApi.getBookList(any(), any(), any()) } returns Single.just(generateResponse(listId = "test-list", totalCount = 30))
        every { booksApi.getBookList(any(), date, any()) } returns Single.error(error)

        mediator.withOptions(BookListMediator.Options.LoadCurrent("test-list"))

        var observer = mediator
            .loadSingle(
                loadType = LoadType.REFRESH,
                state = PagingState(
                    pages = emptyList(),
                    anchorPosition = 0,
                    config = pagingConfig,
                    10
                )
            ).test()

        observer.dispose()

        observer = mediator
            .loadSingle(
                loadType = LoadType.APPEND,
                state = PagingState(
                    pages = emptyList(),
                    anchorPosition = 0,
                    config = pagingConfig,
                    10
                )
            ).test()

        observer.assertValueCount(1)
        observer.values()[0].shouldBeInstanceOf<RemoteMediator.MediatorResult.Error>()
            .throwable
            .shouldBeInstanceOf<RemoteRequestError>().apply {
                cause.shouldBe(error)
                shouldBeInstanceOf(klass)
            }
    }

    companion object {

        @Parameters
        @JvmStatic
        fun parameters(): Iterable<Array<Any>> {
            return listOf(
                arrayOf(RemoteRequestError.NoConnection::class, IOException()),
                arrayOf(RemoteRequestError.ConnectionTimeout::class, SocketTimeoutException()),
                arrayOf(RemoteRequestError.Server::class, HttpException(Response.error<Any>(500, "".toResponseBody()))),
                arrayOf(RemoteRequestError.Unknown::class, IllegalStateException())
            )
        }

    }
}