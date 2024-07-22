package com.example.otchallenge.presentation.presenter

import com.example.otchallenge.domain.executor.PostExecutionThread
import com.example.otchallenge.domain.executor.ThreadExecutor
import com.example.otchallenge.domain.usecase.GetBookDetailsUseCaseContract
import com.example.otchallenge.presentation.view.BookDetailView
import com.example.otchallenge.presentation.view.BookView
import com.example.otchallenge.utils.NetworkException
import com.example.otchallenge.utils.NoConnectivityException
import com.example.otchallenge.utils.TimeoutException
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BookDetailPresenter @Inject constructor(
    private val getBookDetailUseCase: GetBookDetailsUseCaseContract,
    private val compositeDisposable: CompositeDisposable,
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
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
        compositeDisposable.add(
            getBookDetailUseCase.getBookDetails(id)
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(
                    { book ->
                        view?.showBookDetails(book)
                    },
                    { error ->
                        handleError(error)
                    }
                )
        )
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