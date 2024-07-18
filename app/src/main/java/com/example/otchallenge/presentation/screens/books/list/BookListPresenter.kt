package com.example.otchallenge.presentation.screens.books.list

import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.data.repository.BookRepository
import com.example.otchallenge.di.data.MonitorModule
import com.example.otchallenge.presentation.extensions.subscribe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Named

class BookListPresenter @Inject constructor (
    private val bookRepository: BookRepository,
    @Named(MonitorModule.INTERNET_CONNECTION)
    private val internetConnectionMonitor: Observable<Boolean>
) : BookListContract.Presenter {

    private var subscriptions: CompositeDisposable? = null
    private val pageSubject = BehaviorSubject.create<PagingData<Book>>()
    private var view: BookListContract.View? = null

    override fun attachView(
        view: BookListContract.View,
    ) {
        this.view = view
        subscriptions = CompositeDisposable().apply {
            observePageUpdate()
            observeInternetConnectionAvailability()
        }
    }

    override fun detachView() {
        subscriptions?.dispose()
        subscriptions = null
        view = null
    }

    override fun subscribeToList(coroutineScope: CoroutineScope) {
        bookRepository.getBookList(DEFAULT_BOOK_LIST)
            .cachedIn(coroutineScope)
            .subscribe(pageSubject)
    }

    private fun CompositeDisposable.observePageUpdate() {
        pageSubject.subscribe(this) { page ->
            view?.submitPage(page)
        }
    }

    private fun CompositeDisposable.observeInternetConnectionAvailability() {
        internetConnectionMonitor.subscribe(this) { hasInternet ->
            if (hasInternet) {
                view?.retryLoadingIfNecessary()
            }
        }
    }

    companion object {
        private const val DEFAULT_BOOK_LIST = "hardcover-fiction"
    }

}