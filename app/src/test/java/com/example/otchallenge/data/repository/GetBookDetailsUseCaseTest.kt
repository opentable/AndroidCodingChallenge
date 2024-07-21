package com.example.otchallenge.data.repository

import com.example.otchallenge.data.model.MockData
import com.example.otchallenge.domain.usecase.GetBookDetailsUseCase
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.powermock.api.mockito.PowerMockito.`when`

class GetBookDetailsUseCaseTest {
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var bookRepository: BookRepository

    private lateinit var getBookDetailsUseCase: GetBookDetailsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getBookDetailsUseCase = GetBookDetailsUseCase(bookRepository)
    }

    @Test
    fun `getBookDetails should return book detail presentation from repository`() {
        // Arrange
        val book = MockData.createMockBook()
        `when`(bookRepository.loadBookDetails(1)).thenReturn(Single.just(book))

        // Act
        val testObserver = getBookDetailsUseCase.getBookDetails(1).test()

        // Assert
        testObserver.assertComplete()
        testObserver.assertValue { bookDetail ->
            bookDetail.title == "Title 1" && bookDetail.author == "Author 1"
        }

        verify(bookRepository).loadBookDetails(1)
    }

    @Test
    fun `getBookDetails should handle errors`() {
        // Arrange
        val errorMessage = "Database error"
        `when`(bookRepository.loadBookDetails(1)).thenReturn(Single.error(Throwable(errorMessage)))

        // Act
        val testObserver = getBookDetailsUseCase.getBookDetails(1).test()

        // Assert
        testObserver.assertError { it.message == errorMessage }

        verify(bookRepository).loadBookDetails(1)
    }
}