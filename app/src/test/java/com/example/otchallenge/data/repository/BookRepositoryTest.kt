package com.example.otchallenge.data.repository

import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.data.database.BookEntity
import com.example.otchallenge.data.database.BookSummaryEntity
import com.example.otchallenge.data.model.MockData
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.anyList
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
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
    fun `loadBooks should return book summaries from network and cache them`() {
        val mockResponse = MockData.createMockOverviewResponse()
        val response = Response.success(mockResponse)
        val bookEntities = MockData.createMockBookEntities()
        val bookSummaries = MockData.createMockBookSummaryEntities()

        `when`(bookService.getBooks()).thenReturn(Single.just(response))
        `when`(bookDao.insertBooks(anyList())).thenReturn(listOf(1L))
        `when`(bookDao.getBooks()).thenReturn(
            Single.just(
                listOf(
                    BookSummaryEntity(
                        1,
                        "Title",
                        "Description",
                        "BookImage"
                    )
                )
            )
        )


        val testObserver = bookRepository.loadBooks().test()

        // Assertions
        testObserver.assertComplete()
        testObserver.assertValue { books ->
            books.size == 1 && books[0].title == "Title"
        }

        // Verify DAO interactions
        verify(bookDao).insertBooks(anyList())
        verify(bookDao).getBooks()
    }

    @Test
    fun `loadBooks should return cached data on network error`() {
        // Mocking API error response
        `when`(bookService.getBooks()).thenReturn(Single.error(Throwable("Network error")))

        // Mocking DAO behavior
        `when`(bookDao.getBooks()).thenReturn(Single.just(listOf(BookSummaryEntity(1, "Title", "Description", "BookImage"))))

        val testObserver = bookRepository.loadBooks().test()

        // Assertions
        testObserver.assertComplete()
        testObserver.assertValue { books ->
            books.size == 1 && books[0].title == "Title"
        }

        // Verify DAO interactions
        verify(bookDao, never()).insertBooks(anyList())
        verify(bookDao).getBooks()
    }

    @Test
    fun `loadBookDetails should return book details from database`() {
        // Mocking DAO behavior
        val bookEntity = BookEntity(1, 1, 1, 1, "1234567890", "1234567890123", "Publisher", "Description", "10.99", "Title", "Author", "Contributor", "BookImage", 500, 800, "AmazonProductUrl", "AgeGroup", "BookUri")
        `when`(bookDao.getBookById(1)).thenReturn(Single.just(bookEntity))

        val testObserver = bookRepository.loadBookDetails(1).test()

        // Assertions
        testObserver.assertComplete()
        testObserver.assertValue { book ->
            book.title == "Title"
        }

        // Verify DAO interactions
        verify(bookDao).getBookById(1)
    }

    @Test
    fun `loadBooks should return books from database when service fails`() {
        val bookSummaries = MockData.createMockBookSummaryEntities()
        val throwable = Throwable("Service error")

        `when`(bookService.getBooks()).thenReturn(Single.error(throwable))
        `when`(bookDao.getBooks()).thenReturn(Single.just(bookSummaries))

        bookRepository.loadBooks()
            .test()
            .assertValue { books ->
                books.size == 10 && books[0].title == "Title 1"
            }
    }

    @Test
    fun `loadBookDetails should return book from database`() {
        val bookEntity = MockData.createMockBookEntities()[0]

        `when`(bookDao.getBookById(1)).thenReturn(Single.just(bookEntity))

        bookRepository.loadBookDetails(1)
            .test()
            .assertValue { book ->
                book.title == "Title 1"
            }
    }
}