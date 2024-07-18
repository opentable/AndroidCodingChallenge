package com.example.otchallenge.presentation.screens.books.list

import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.data.repository.BookRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class BookListPresenter @Inject constructor (
    private val bookRepository: BookRepository
) : BookListContract.Presenter {

    private lateinit var disposables: CompositeDisposable
    private val pageSubject = BehaviorSubject.create<PagingData<Book>>()
    private var view: BookListContract.View? = null

    override fun attachView(
        view: BookListContract.View,
    ) {
        this.view = view
        disposables = CompositeDisposable()
        observePageUpdate()
    }

    override fun detachView() {
        disposables.dispose()
        view = null
    }

    override fun subscribeToList(coroutineScope: CoroutineScope) {
        bookRepository.getBookList(DEFAULT_BOOK_LIST)
            .cachedIn(coroutineScope)
            .subscribe(pageSubject)
    }

    private fun observePageUpdate() {
        pageSubject
            .subscribe { page ->
                view?.submitPage(page)
            }.also { disposable ->
                disposables.add(disposable)
            }
    }

    companion object {
        private const val DEFAULT_BOOK_LIST = "hardcover-fiction"
    }

}