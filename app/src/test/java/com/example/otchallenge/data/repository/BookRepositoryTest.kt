package com.example.otchallenge.data.repository

import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.data.model.MockData
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import retrofit2.Response

class BookRepositoryTest {

    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var bookService: BooksService

    @Mock
    lateinit var bookDao: BookDao

    private lateinit var bookRepository: BookRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        bookRepository = BookRepository(bookService, bookDao)
    }


    @Test
    fun `loadBooks should return books when service is successful`() {
        val mockResponse = MockData.createMockOverviewResponse()
        val response = Response.success(mockResponse)
        val bookEntities = MockData.createMockBookEntities()
        val bookSummaries = MockData.createMockBookSummaryEntities()

        Mockito.`when`(bookService.getBooks()).thenReturn(Single.just(response))
        Mockito.doNothing().`when`(bookDao).insertBooks(bookEntities)
        Mockito.`when`(bookDao.getBooks()).thenReturn(Single.just(bookSummaries))

        bookRepository.loadBooks()
            .test()
            .assertValue { books ->
                books.size == 10 && books[0].title == "Title 1"
            }
    }

    @Test
    fun `loadBooks should return books from database when service fails`() {
        val bookSummaries = MockData.createMockBookSummaryEntities()
        val throwable = Throwable("Service error")

        Mockito.`when`(bookService.getBooks()).thenReturn(Single.error(throwable))
        Mockito.`when`(bookDao.getBooks()).thenReturn(Single.just(bookSummaries))

        bookRepository.loadBooks()
            .test()
            .assertValue { books ->
                books.size == 10 && books[0].title == "Title 1"
            }
    }

    @Test
    fun `loadBookDetails should return book from database`() {
        val bookEntity = MockData.createMockBookEntities()[0]

        Mockito.`when`(bookDao.getBookById(1)).thenReturn(Single.just(bookEntity))

        bookRepository.loadBookDetails(1)
            .test()
            .assertValue { book ->
                book.title == "Title 1"
            }
    }
}