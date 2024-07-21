package com.example.otchallenge.presentation.presenter

import android.util.Log
import com.example.otchallenge.domain.usecase.GetBookDetailsUseCaseContract
import com.example.otchallenge.presentation.view.BookDetailView
import com.example.otchallenge.presentation.view.BookView
import com.example.otchallenge.utils.*
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject
import javax.inject.Named

class BookDetailPresenter @Inject constructor(
    private val getBookDetailUseCase: GetBookDetailsUseCaseContract,
    private val compositeDisposable: CompositeDisposable,
    @Named("io") private val ioScheduler: Scheduler
) : BookDetailPresenterContract {

    private var view: BookDetailView? = null

    override fun attachView(view: BookView) {
        this.view = view as BookDetailView
    }

    override fun detachView() {
        this.view = null
        clearDisposables()
    }

    override fun loadBookDetails(id: Int) {
        Log.d("BookDetailPresenter", "loadBookDetails called with id: $id")
        val disposable = getBookDetailUseCase.getBookDetails(id)
            .subscribeOn(ioScheduler)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { Log.d("BookDetailPresenter", "loadBookDetails: Subscribed") }
            .doOnSuccess { Log.d("BookDetailPresenter", "loadBookDetails: Success - $it") }
            .doOnError { Log.e("BookDetailPresenter", "loadBookDetails: Error", it) }
            .doOnDispose { Log.d("BookDetailPresenter", "loadBookDetails: Disposed") }
            .subscribe(
                { book ->
                    Log.d("BookDetailPresenter", "Book details loaded: $book")
                    view?.showBookDetails(book)
                },
                { error ->
                    Log.e("BookDetailPresenter", "Error loading book details", error)
                    handleError(error)
                }
            )


        compositeDisposable.add(disposable)
        Log.d("BookDetailPresenter", "Disposable added to compositeDisposable")
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