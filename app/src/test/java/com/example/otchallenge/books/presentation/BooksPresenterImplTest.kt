package com.example.otchallenge.books.presentation

import com.example.otchallenge.books.data.Isbn
import com.example.otchallenge.books.data.NYTimeBook
import com.example.otchallenge.books.domain.BooksFetcher
import com.example.otchallenge.books.domain.FakeBooksRepository
import com.example.otchallenge.books.domain.FakeStringResources
import com.example.otchallenge.books.domain.TimeCalculator
import io.reactivex.schedulers.Schedulers
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.Locale

class BooksPresenterImplTest {

    private lateinit var fakeBooksRepository: FakeBooksRepository
    private val timeCalculator = TimeCalculator(Locale.getDefault())
    private val fakeStringResources = FakeStringResources()
    private lateinit var booksFetcher: BooksFetcher
    private lateinit var booksPresenter: BookPresenter
    private lateinit var fakeBooksView: FakeBooksView

    private val nyTimeBook = NYTimeBook(
        primaryIsbn10 = "1",
        publisher = "Publisher",
        description = "Description",
        title = "Title1",
        author = "Author",
        bookImage = "www.google.com",
        bookImageWidth = 256,
        bookImageHeight = 256,
        isbns = listOf(Isbn("123", "456"))
    )

    @Before
    fun setup() {
        fakeBooksRepository = FakeBooksRepository()
        booksFetcher = BooksFetcher(
            booksRepository = fakeBooksRepository,
            timeCalculator = timeCalculator,
            stringResources = fakeStringResources
        )
        fakeBooksView = FakeBooksView()
        booksPresenter = BooksPresenterImpl(
            booksFetcher = booksFetcher,
            booksView = fakeBooksView,
            stringResources = fakeStringResources,
            observeOnScheduler = Schedulers.single()
        )
    }

    @Test
    fun `given books are saved locally, then books are shown`() {
        fakeBooksRepository.insertBooks(
            lastModified = "2024-07-17T22:24:46-04:00",
            books = listOf(nyTimeBook)
        )
        booksPresenter.fetchBooksFromLocal()
        Thread.sleep(100)
        Assert.assertEquals("Title1", fakeBooksView.books.first().title)
        Assert.assertEquals(fakeStringResources.lastModificationDate, fakeBooksView.showLastModifiedDate)
        Assert.assertFalse(fakeBooksView.showNoBooksFound)
        Assert.assertTrue(fakeBooksView.showTryAgainError.isEmpty())
    }

    @Test
    fun `given books never are saved locally, then books are shown`() {
        booksPresenter.fetchBooksFromLocal()
        Thread.sleep(100)
        Assert.assertTrue(fakeBooksView.books.isEmpty())
        Assert.assertTrue(fakeBooksView.showLastModifiedDate.isEmpty())
        Assert.assertFalse(fakeBooksView.showNoBooksFound)
        Assert.assertTrue(fakeBooksView.showTryAgainError.isEmpty())
    }

    @Test
    fun `given no books are fetched from remote, then showNoBooksFound is shown`() {
        booksPresenter.fetchBooksFromRemote(0)
        Thread.sleep(100)
        Assert.assertTrue(fakeBooksView.books.isEmpty())
        Assert.assertTrue(fakeBooksView.showLastModifiedDate.isEmpty())
        Assert.assertTrue(fakeBooksView.showNoBooksFound)
        Assert.assertTrue(fakeBooksView.showTryAgainError.isEmpty())
    }

    @Test
    fun `given an error occurred while fetching from local, then error is shown`() {
        fakeBooksRepository.throwUnknownErrorFromRemote = true
        booksPresenter.fetchBooksFromLocal()
        Thread.sleep(100)
        Assert.assertTrue(fakeBooksView.books.isEmpty())
        Assert.assertTrue(fakeBooksView.showLastModifiedDate.isEmpty())
        Assert.assertFalse(fakeBooksView.showNoBooksFound)
        Assert.assertFalse(fakeBooksView.showTryAgainError.isEmpty())
    }

    @Test
    fun `given an error occurred while fetching from remote, then error is shown`() {
        fakeBooksRepository.throwUnknownErrorFromRemote = true
        booksPresenter.fetchBooksFromRemote(0)
        Thread.sleep(100)
        Assert.assertTrue(fakeBooksView.books.isEmpty())
        Assert.assertTrue(fakeBooksView.showLastModifiedDate.isEmpty())
        Assert.assertFalse(fakeBooksView.showNoBooksFound)
        Assert.assertFalse(fakeBooksView.showTryAgainError.isEmpty())
    }
}