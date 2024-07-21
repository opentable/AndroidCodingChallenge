package com.example.otchallenge.domain.usecase

import com.example.otchallenge.data.model.MockData
import com.example.otchallenge.data.repository.BookRepository
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
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
        val bookSummaries = MockData.createMockBookSummaries()

        Mockito.`when`(bookRepository.loadBooks()).thenReturn(Single.just(bookSummaries))

        getBooksUseCase.execute()
            .test()
            .assertValue { books ->
                books.size == 10 && books[0].title == "Title 1"
            }
    }

    @Test
    fun `getBookDetails should return BookDetailPresentation`() {
        val book = MockData.createMockBook()

        Mockito.`when`(bookRepository.loadBookDetails(1)).thenReturn(Single.just(book))

        getBooksUseCase.getBookDetails(1)
            .test()
            .assertValue { bookDetail ->
                bookDetail.title == "Title 1" && bookDetail.id == 1
            }
    }
}