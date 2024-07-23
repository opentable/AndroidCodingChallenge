package com.example.otchallenge.books.presentation

import com.care.sdk.utils.StringResources
import com.example.otchallenge.R
import com.example.otchallenge.books.domain.BooksData
import com.example.otchallenge.books.domain.BooksFetcher
import com.example.otchallenge.books.domain.toBookUiModel
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject


class BooksPresenterImpl @Inject constructor(
    private val booksFetcher: BooksFetcher,
    private val booksView: BooksView,
    private val stringResources: StringResources,
    private val observeOnScheduler: Scheduler
): BookPresenter {

    override fun fetchBooksFromLocal(): Disposable {
        return booksFetcher.fetchBooksFromLocal()
            .subscribeOn(Schedulers.io())
            .observeOn(observeOnScheduler)
            .subscribe(::onLocalBooksSuccess, ::onBooksError)
    }

    override fun fetchBooksFromRemote(offset: Int): Disposable {
        booksView.setLoading(true)
        return booksFetcher.fetchBooksFromRemote(offset)
            .subscribeOn(Schedulers.io())
            .observeOn(observeOnScheduler)
            .subscribe(::onRemoteBooksSuccess, ::onBooksError)
    }

    override fun retryLastFetch(): Disposable {
        booksView.setLoading(true)
        return booksFetcher.retryLastFetch()
            .subscribeOn(Schedulers.io())
            .observeOn(observeOnScheduler)
            .subscribe(::onRemoteBooksSuccess, ::onBooksError)
    }

    private fun onLocalBooksSuccess(data: BooksData) {
        Timber.i("books fetched successfully ${data.books.size}")
        booksView.setLoading(false)
        val uiBooks = data.books.map { book -> book.toBookUiModel(stringResources) }
        booksView.showBooks(uiBooks)
        booksView.showLastModifiedDate(stringResources.getString(R.string.last_modification, data.lastModified))
    }

    private fun onRemoteBooksSuccess(isBooksEmpty: Boolean) {
        Timber.i("books fetched successfully $isBooksEmpty")
        booksView.setLoading(false)

        if (isBooksEmpty) {
            booksView.showNoBooksFound()
            return
        }
    }

    private fun onBooksError(error: Throwable) {
        Timber.i("books fetched error ${error.message}")
        booksView.showTryAgainError(error.message.orEmpty())
        booksView.setLoading(false)
    }
}

interface BookPresenter {
    fun fetchBooksFromLocal(): Disposable
    fun fetchBooksFromRemote(offset: Int): Disposable
    fun retryLastFetch(): Disposable
}

interface BooksView {
    fun showBooks(books: List<BookUiModel>)
    fun showLastModifiedDate(date: String)
    fun showNoBooksFound()
    fun showTryAgainError(error: String)
    fun setLoading(isLoading: Boolean)
}