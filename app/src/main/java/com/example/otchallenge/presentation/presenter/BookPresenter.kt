package com.example.otchallenge.presentation.presenter

import com.example.otchallenge.domain.usecase.GetBooksUseCaseContract
import com.example.otchallenge.presentation.view.BookView
import com.example.otchallenge.utils.ConnectivityChecker
import com.example.otchallenge.utils.NetworkException
import com.example.otchallenge.utils.NoConnectivityException
import com.example.otchallenge.utils.TimeoutException
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

class BookPresenter @Inject constructor(
    private val getBooksUseCase: GetBooksUseCaseContract,
    private val connectivityChecker: ConnectivityChecker,
    private val compositeDisposable: CompositeDisposable,
    @Named("io") private val ioScheduler: Scheduler
) : BookPresenterContract {

    private var view: BookView? = null

    fun attachView(view: BookView) {
        this.view = view
    }

    fun detachView() {
        this.view = null
    }

    override fun loadBooks() {
        val disposable = getBooksUseCase.execute()
            .subscribeOn(ioScheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { books ->
                    view?.showBooks(books)
                },
                { error ->
                    handleError(error)
                }
            )

        compositeDisposable.add(disposable)
    }

    override fun loadBookDetails(id: Int) {
        val disposable = getBooksUseCase.getBookDetails(id)
            .subscribeOn(ioScheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { book ->
                    view?.showBookDetails(book)
                },
                { error ->
                    handleError(error)
                }
            )
        compositeDisposable.add(disposable)
    }

    private fun handleError(error: Throwable) {
        when (error) {
            is NoConnectivityException -> view?.showError("No internet connection")
            is TimeoutException -> view?.showError("Connection timeout")
            is NetworkException -> view?.showError("Network error")
            else -> view?.showError("An unknown error occurred")
        }
    }

    override fun clearDisposables() {
        compositeDisposable.clear()
    }
}