package com.example.otchallenge.data.repository

import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.data.mapper.BookApiMapper
import com.example.otchallenge.data.model.MockData
import com.example.otchallenge.domain.mapper.BookSummaryEntityMapper
import com.example.otchallenge.domain.repository.BookListRepository
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.powermock.api.mockito.PowerMockito.`when`
import retrofit2.HttpException
import retrofit2.Response

class BookListRepositoryImplTest {
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var bookService: BooksService

    @Mock
    private lateinit var bookDao: BookDao

    private lateinit var apiMapper: BookApiMapper
    private lateinit var mapper: BookSummaryEntityMapper
    private lateinit var bookListRepository: BookListRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        apiMapper = BookApiMapper()
        mapper = BookSummaryEntityMapper()
        bookListRepository = BookListRepositoryImpl(bookService, bookDao)
    }

    @Test
    fun `getBooks should return book summaries when API call is successful`() {
        // Arrange
        val bookEntities = MockData.createMockBookSummaryEntities()
        val bookSummary = MockData.createMockBookSummaries()
        val apiResponse = MockData.createMockOverviewResponse()

        `when`(bookService.getBooks()).thenReturn(Single.just(Response.success(apiResponse)))
        `when`(bookDao.insertBooks(anyList())).thenReturn(Completable.complete())
        `when`(bookDao.getBooks()).thenReturn(Single.just(bookEntities))

        // Act
        val testObserver = bookListRepository.getBooks()
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .test()

        // Assert
        testObserver.assertValue(bookSummary)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun `getBooks should return error when API call fails`() {
        // Arrange
        val throwable = HttpException(
            Response.error<Any>(
                404,
                ResponseBody.create(MediaType.get("application/json"), "{}")
            )
        )

        `when`(bookService.getBooks()).thenReturn(Single.error(throwable))
        `when`(bookDao.getBooks()).thenReturn(Single.error(throwable))

        // Act
        val testObserver = bookListRepository.getBooks()
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .test()

        // Assert
        testObserver.assertError(throwable)
        testObserver.assertNotComplete()
    }

    @Test
    fun `getBooks should return cached book summaries when API call fails`() {
        // Arrange
        val bookEntity = MockData.createMockBookSummaryEntities()
        val bookSummary = MockData.createMockBookSummaries()
        val throwable = Throwable("Network error")

        `when`(bookService.getBooks()).thenReturn(Single.error(throwable))
        `when`(bookDao.getBooks()).thenReturn(Single.just(bookEntity))

        // Act
        val testObserver = bookListRepository.getBooks()
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .test()

        // Assert
        testObserver.assertValue(bookSummary)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }
}