package com.example.otchallenge.presentation.screens.books.list

import android.util.Log
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.otchallenge.data.model.Book
import com.example.otchallenge.data.paging.booklist.BookListMediator
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

    private var isCurrentErrorDetailsShown = false

    private var isInNetworkErrorState = false

    override fun attachView(
        view: BookListContract.View,
    ) {
        this.view = view
        subscriptions = CompositeDisposable().apply {
            observePageUpdate()
            observeInternetConnectionAvailability()
        }

        pageSubject.value?.let {
            view.submitPage(it)
        }
    }

    override fun detachView() {
        subscriptions?.dispose()
        subscriptions = null
        view = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun subscribeToList(coroutineScope: CoroutineScope) {
        bookRepository.getBookList(
            BookListMediator.Options.LoadCurrent(DEFAULT_BOOK_LIST)
        )
            .cachedIn(coroutineScope)
            .subscribe(pageSubject)
    }

    override fun updateListState(isEmpty: Boolean, loadStates: CombinedLoadStates) {
        when  {
            loadStates.mediator?.isLoading == true -> {
                if (loadStates.refresh is LoadState.Loading) {
                    displayRefreshState(isListEmpty = isListEmpty())
                }
                isInNetworkErrorState = false
                isCurrentErrorDetailsShown = false
            }
            loadStates.mediator?.hasError == true -> {
                Log.e("Hola", "Hola", loadStates.error)
                loadStates.error?.let { error ->
                    isInNetworkErrorState = error is RemoteRequestError.NoConnection
                    displayErrorState(isListEmpty = isListEmpty(), error = error)
                }
            }
            loadStates.mediator?.isIdle == true -> {
                view?.showIdleState()
            }
        }
    }

    private fun isListEmpty(): Boolean {
        return (view?.listItemCount() ?: 0) == 0
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
            view?.submitPage(page)
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

    companion object {
        private const val DEFAULT_BOOK_LIST = "hardcover-fiction"
    }

}