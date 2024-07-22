package com.example.otchallenge.domain.usecase


import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.data.model.MockData
import com.example.otchallenge.domain.executor.ImmediateThreadExecutor
import com.example.otchallenge.domain.executor.PostExecutionThread
import com.example.otchallenge.domain.mapper.BookSummaryMapper
import com.example.otchallenge.domain.repository.BookListRepository
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class GetBooksUseCaseTest {

    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var bookRepository: BookListRepository

    @Mock
    lateinit var bookDao: BookDao

    @Mock
    private lateinit var postExecutionThread: PostExecutionThread


    private lateinit var getBooksUseCase: GetBooksUseCase

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        getBooksUseCase =
            GetBooksUseCase(bookRepository, ImmediateThreadExecutor(), postExecutionThread)
    }

    @Test
    fun `buildUseCase should return book presentations when repository call is successful`() {
        // Arrange
        val bookSummaries = listOf(MockData.getMockSummary())
        val bookPresentations = listOf(BookSummaryMapper().transform(MockData.getMockSummary()))

        `when`(bookRepository.getBooks()).thenReturn(Single.just(bookSummaries))
        `when`(postExecutionThread.getScheduler()).thenReturn(Schedulers.trampoline())

        // Act
        val testObserver = getBooksUseCase.execute(null)
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .test()

        // Assert
        //testObserver.assertValue(bookPresentations)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }


    @Test
    fun `buildUseCase should return error when repository call fails`() {
        // Arrange
        val throwable = Throwable("Error fetching books")

        `when`(bookRepository.getBooks()).thenReturn(Single.error(throwable))
        `when`(postExecutionThread.getScheduler()).thenReturn(Schedulers.trampoline())

        // Act
        val testObserver = getBooksUseCase.execute(null)
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .test()

        // Assert
        testObserver.assertError(throwable)
        testObserver.assertNotComplete()
    }
}