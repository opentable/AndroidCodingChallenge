package com.example.otchallenge.presentation.screens.books.list

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.otchallenge.data.model.Book
import com.example.otchallenge.data.remote.RemoteRequestError
import com.example.otchallenge.data.repository.BookRepository
import com.example.otchallenge.di.data.MonitorModule
import com.example.otchallenge.presentation.extensions.error
import com.example.otchallenge.presentation.extensions.isLoading
import com.example.otchallenge.presentation.extensions.subscribe
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

class BookListPresenter @Inject constructor (
    private val bookRepository: BookRepository,
    @Named(MonitorModule.INTERNET_CONNECTION)
    private val internetConnectionMonitor: Observable<Boolean>
) : BookListContract.Presenter {

    private var subscriptions: CompositeDisposable? = null
    private val pageSubject = BehaviorSubject.create<PagingData<Book>>()
    private val listTypeSubject = BehaviorSubject.create<BookListContract.ListType>()
    private var view: BookListContract.View? = null

    private var lastShownError: Throwable? = null
    private var isCurrentErrorDetailsShown = false

    private var isInNetworkErrorState = false

    override fun attachView(view: BookListContract.View) {
        this.view = view

        subscriptions = CompositeDisposable().apply {
            observeBookListUpdate()
            observePageUpdate()
            observeInternetConnectionAvailability()
        }

        pageSubject.value?.let {
            view.submitPagingData(it)
        }
    }

    override fun detachView() {
        subscriptions?.dispose()
        subscriptions = null
        view = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun subscribeToList(
        coroutineScope: CoroutineScope,
        bookListId: String?,
        date: LocalDate?
    ) {
        bookRepository.getBookList(bookListId)
            .doOnSuccess { item ->
                listTypeSubject.onNext(
                    if (date == null) {
                        BookListContract.ListType.Current(item)
                    } else {
                        BookListContract.ListType.Date(item, date)
                    }
                )
            }
            .flatMapObservable {
                bookRepository.getBookListPages(
                    bookListId = it.id,
                    date = date
                ).cachedIn(coroutineScope)
            }
            .subscribe(pageSubject)
    }

    override fun updateListState(isEmpty: Boolean, loadStates: CombinedLoadStates) {
        when  {
            loadStates.mediator?.isLoading == true -> {
                if (loadStates.refresh is LoadState.Loading) {
                    displayRefreshState(isListEmpty = isEmpty)
                }
                isInNetworkErrorState = false
                isCurrentErrorDetailsShown = false
            }
            loadStates.mediator?.hasError == true -> {
                loadStates.error?.let { error ->
                    isInNetworkErrorState = error is RemoteRequestError.NoConnection
                    if (lastShownError != error) {
                        lastShownError = error
                        displayErrorState(isListEmpty = isEmpty, error = error)
                    }
                }
            }
            loadStates.mediator?.isIdle == true -> {
                view?.showIdleState()
            }
        }
    }

    override fun onErrorDialogDismissed() {
        isCurrentErrorDetailsShown = false
    }

    private fun displayRefreshState(isListEmpty: Boolean) {
        if (isListEmpty) {
            view?.showFullScreenRefreshState()
        } else {
            view?.showRefreshIndicator()
        }
    }

    private fun displayErrorState(isListEmpty: Boolean, error: Throwable?) {
        when {
            isListEmpty -> {
                view?.showFullScreenErrorState(error)
            }
            !isCurrentErrorDetailsShown -> {
                isCurrentErrorDetailsShown = true
                view?.showErrorDialog(error)
            }
        }
    }

    private fun CompositeDisposable.observePageUpdate() {
        pageSubject.subscribe(this) { page ->
            view?.submitPagingData(page)
        }
    }

    private fun CompositeDisposable.observeBookListUpdate() {
        listTypeSubject.subscribe(this) { type ->
            view?.setActionBar(type)
        }
    }

    private fun CompositeDisposable.observeInternetConnectionAvailability() {
        internetConnectionMonitor.subscribe(this) { hasInternet ->
            if (hasInternet && isInNetworkErrorState) {
                if (isCurrentErrorDetailsShown) {
                    view?.dismissErrorDialog()
                }
                view?.retryLoading()
            }
        }
    }
}