package com.example.otchallenge.books.domain

import com.example.otchallenge.books.data.Isbn
import com.example.otchallenge.books.data.NYTimeBook
import com.example.otchallenge.network.NetworkError
import com.example.otchallenge.network.NoResponseBodyError
import org.junit.Before
import org.junit.Test
import java.util.Locale

class BooksFetcherTest {

    private lateinit var fakeBooksRepository: FakeBooksRepository
    private val timeCalculator = TimeCalculator(Locale.getDefault())
    private val fakeStringResources = FakeStringResources()
    private lateinit var booksFetcher: BooksFetcher

    private val book = Book(
        publisher = "Publisher",
        description = "Description",
        title = "Title1",
        author = "Author",
        image = "www.google.com",
        imageWidth = 256,
        imageHeight = 256,
        isbns = "123, 456"
    )

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
    }

    @Test
    fun `given offset is less than biggestOffset, then fetchBooksFromRemote return false`() {
        booksFetcher.fetchBooksFromRemote(-2)
            .test()
            .assertResult(false)
    }

    @Test
    fun `given offset is bigger than biggestOffset, then fetchBooksFromRemote return true`() {
        booksFetcher.fetchBooksFromRemote(0)
            .test()
            .assertResult(true)
    }

    @Test
    fun `given offset less than previous valid offset, then fetchBooksFromRemote return false`() {
        booksFetcher.fetchBooksFromRemote(1).subscribe()
        booksFetcher.fetchBooksFromRemote(0)
            .test()
            .assertResult(false)
    }

    @Test
    fun `given an unknown error occurred, then fetchBooksFromRemote return NetworkError`() {
        fakeBooksRepository.throwUnknownErrorFromRemote = true
        booksFetcher.fetchBooksFromRemote(0)
            .test()
            .assertFailure(NetworkError::class.java)
            .assertErrorMessage(fakeStringResources.networkError)
    }

    @Test
    fun `given a NoResponseBodyError error occurred, then fetchBooksFromRemote return NoResponseBodyError`() {
        fakeBooksRepository.throwNoResBodyErrorFromRemote = true
        booksFetcher.fetchBooksFromRemote(0)
            .test()
            .assertFailure(NoResponseBodyError::class.java)
            .assertErrorMessage(fakeStringResources.noResponseBodyError)
    }

    @Test
    fun `given we have books saved on local, then fetchBooksFromLocal return books`() {
        val booksData = BooksData(
            lastModified = "Jul-17-2024 08:24 PM",
            books = listOf(book)
        )
        fakeBooksRepository.insertBooks(
            lastModified = "2024-07-17T22:24:46-04:00",
            books = listOf(nyTimeBook)
        )
        booksFetcher.fetchBooksFromLocal()
            .test()
            .assertValue(booksData)
    }

    @Test
    fun `given no books saved on local, then fetchBooksFromLocal return zero books`() {
        val booksData = BooksData(
            lastModified = "",
            books = listOf()
        )
        booksFetcher.fetchBooksFromLocal()
            .test()
            .assertValue(booksData)
    }

    @Test
    fun `given an unknown error occurred, then fetchBooksFromLocal return specific NetworkError`() {
        fakeBooksRepository.throwUnknownErrorFromRemote = true
        booksFetcher.fetchBooksFromLocal()
            .test()
            .assertFailure(NetworkError::class.java)
            .assertErrorMessage(fakeStringResources.networkError)
    }

    @Test
    fun `given a NoResponseBodyError error occurred, then fetchBooksFromLocal return specific NoResponseBodyError`() {
        fakeBooksRepository.throwNoResBodyErrorFromRemote = true
        booksFetcher.fetchBooksFromLocal()
            .test()
            .assertFailure(NoResponseBodyError::class.java)
            .assertErrorMessage(fakeStringResources.noResponseBodyError)
    }

}