package com.example.otchallenge.presentation.screens.books.list

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.example.otchallenge.data.model.Book
import kotlinx.coroutines.CoroutineScope

interface BookListContract {

    interface View {
        fun submitPage(page: PagingData<Book>)
        fun retryLoading()
        fun listItemCount(): Int
        fun showIdleState()
        fun showFullScreenRefreshState()
        fun showRefreshIndicator()
        fun showFullScreenErrorState(error: Throwable?)
        fun showErrorDialog(error: Throwable?)
        fun dismissErrorDialog()
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun subscribeToList(coroutineScope: CoroutineScope)
        fun updateListState(isEmpty: Boolean, loadStates: CombinedLoadStates)
    }

}