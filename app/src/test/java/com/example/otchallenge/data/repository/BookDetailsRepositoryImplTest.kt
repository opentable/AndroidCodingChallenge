package com.example.otchallenge.data.repository

import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.data.model.MockData
import com.example.otchallenge.domain.mapper.BookEntityMapper
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

class BookDetailsRepositoryImplTest {
    @get:Rule
    val rule: MockitoRule = MockitoJUnit.rule()

    @Mock
    private lateinit var bookDao: BookDao

    @Mock
    private lateinit var mapper: BookEntityMapper

    private lateinit var bookDetailsRepository: BookDetailsRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        bookDetailsRepository = BookDetailsRepositoryImpl(bookDao)
    }

    @Test
    fun `loadBookDetails should return book details when book is found`() {
        // Arrange
        val bookEntity = MockData.getMockBookEntity()
        val bookDomain = MockData.getMockBookDomain()

        `when`(bookDao.getBookById(bookEntity.id)).thenReturn(Single.just(bookEntity))

        // Act
        val testObserver = bookDetailsRepository.loadBookDetails(bookEntity.id)
            .subscribeOn(Schedulers.trampoline())
            .observeOn(Schedulers.trampoline())
            .test()

        // Assert
        testObserver.assertValue(bookDomain)
        testObserver.assertComplete()
        testObserver.assertNoErrors()
    }
}