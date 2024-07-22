package com.example.otchallenge.presentation.screens.books.list

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import com.example.otchallenge.data.model.Book
import com.example.otchallenge.data.model.BookList
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate

interface BookListContract {

    interface View {
        fun setActionBar(listType: ListType)
        fun submitPagingData(page: PagingData<Book>)
        fun retryLoading()
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
        fun subscribeToList(
            coroutineScope: CoroutineScope,
            bookListId: String?,
            date: LocalDate?
        )
        fun updateListState(isEmpty: Boolean, loadStates: CombinedLoadStates)
        fun onErrorDialogDismissed()
    }

    sealed interface ListType {
        val bookList: BookList
        data class Current(override val bookList: BookList): ListType
        data class Date(override val bookList: BookList, val date: LocalDate): ListType
    }
}