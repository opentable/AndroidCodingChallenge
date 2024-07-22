package com.example.otchallenge.domain.usecase

import com.example.otchallenge.data.model.MockData
import com.example.otchallenge.domain.executor.ImmediateThreadExecutor
import com.example.otchallenge.domain.executor.PostExecutionThread
import com.example.otchallenge.domain.mapper.BookDetailMapper
import com.example.otchallenge.domain.repository.BookDetailsRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.powermock.api.mockito.PowerMockito.`when`

class GetBookDetailsUseCaseTest {
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var bookRepository: BookDetailsRepository

    @Mock
    private lateinit var postExecutionThread: PostExecutionThread

    @Mock
    private lateinit var mapper: BookDetailMapper

    private lateinit var getBookDetailsUseCase: GetBookDetailsUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getBookDetailsUseCase =
            GetBookDetailsUseCase(bookRepository, ImmediateThreadExecutor(), postExecutionThread)
    }

    @Test
    fun `buildUseCase should return book details when book is found`() {
        // Arrange
        val bookEntity = MockData.getMockBookDomain()
        val bookDetailPresentation = MockData.getBookDetailPresentation()

        `when`(bookRepository.loadBookDetails(bookEntity.id!!)).thenReturn(Single.just(bookEntity))
        `when`(mapper.transform(bookEntity)).thenReturn(bookDetailPresentation)
        `when`(postExecutionThread.getScheduler()).thenReturn(Schedulers.trampoline())

        // Act
        val testObserver = getBookDetailsUseCase.execute(bookEntity.id)
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .test()

        // Assert
        testObserver.assertValue(bookDetailPresentation)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }

    @Test
    fun `buildUseCase should return error when book is not found`() {
        // Arrange
        val bookId = 1
        val throwable = Throwable("Book not found")

        `when`(bookRepository.loadBookDetails(bookId)).thenReturn(Single.error(throwable))
        `when`(postExecutionThread.getScheduler()).thenReturn(Schedulers.trampoline())

        // Act
        val testObserver = getBookDetailsUseCase.execute(bookId)
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .test()

        // Assert
        testObserver.assertError(throwable)
        testObserver.assertNotComplete()
    }

    @Test
    fun `buildUseCase should return error when params is null`() {
        // Act
        val testObserver = getBookDetailsUseCase.execute(null)
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .test()

        // Assert
        testObserver.assertError(IllegalArgumentException::class.java)
        testObserver.assertNotComplete()
    }
}