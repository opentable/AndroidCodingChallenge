package com.example.otchallenge.presentation.screens.books.list

import androidx.paging.PagingData
import com.example.otchallenge.data.entity.Book
import kotlinx.coroutines.CoroutineScope

interface BookListContract {

    interface View {
        fun submitPage(page: PagingData<Book>)
        fun retryLoadingIfNecessary()
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun subscribeToList(coroutineScope: CoroutineScope)
    }

}