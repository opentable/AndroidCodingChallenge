package com.example.otchallenge.domain.usecase

import com.example.otchallenge.data.repository.BookRepository
import com.example.otchallenge.domain.model.BookSummary
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class GetBooksUseCaseTest {

    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var bookRepository: BookRepository

    private lateinit var getBooksUseCase: GetBooksUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getBooksUseCase = GetBooksUseCase(bookRepository)
    }

    @Test
    fun `execute should return list of BookPresentation`() {

        val bookSummaries = BookSummary(1, "Title", "Description", "BookImage")
        `when`(bookRepository.loadBooks()).thenReturn(Single.just(listOf(bookSummaries)))

        // Act
        val testObserver = getBooksUseCase.execute().test()

        // Assert
        testObserver.assertComplete()
        testObserver.assertValue { books ->
            books.size == 1 && books[0].title == "Title"
        }

        verify(bookRepository).loadBooks()
    }

    @Test
    fun `execute should handle errors`() {
        // Arrange
        val errorMessage = "Network error"
        `when`(bookRepository.loadBooks()).thenReturn(Single.error(Throwable(errorMessage)))

        // Act
        val testObserver = getBooksUseCase.execute().test()

        // Assert
        testObserver.assertError { it.message == errorMessage }

        verify(bookRepository).loadBooks()
    }
}