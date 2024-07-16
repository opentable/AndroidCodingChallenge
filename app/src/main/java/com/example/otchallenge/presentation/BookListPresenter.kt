package com.example.otchallenge.presentation

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
) : HardcoverFictionListContract.Presenter {

    private lateinit var disposables: CompositeDisposable
    private val pageSubject = BehaviorSubject.create<PagingData<Book>>()
    private var view: HardcoverFictionListContract.View? = null

    override fun attachView(
        view: HardcoverFictionListContract.View,
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
        bookRepository.getHardcoverFictionList()
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

}