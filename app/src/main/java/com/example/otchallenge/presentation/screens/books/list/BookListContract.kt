package com.example.otchallenge.presentation.screens.books.list

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.example.otchallenge.data.model.Book
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate

interface BookListContract {

    interface View {
        fun setActionBar()
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
        val listName: String
        val date: LocalDate
        val isDateToday: Boolean
        fun attachView(view: View)
        fun detachView()
        fun subscribeToList(coroutineScope: CoroutineScope)
        fun updateListState(isEmpty: Boolean, loadStates: CombinedLoadStates)
        fun onErrorDialogDismissed()
    }

}