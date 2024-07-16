package com.example.otchallenge.presentation

import androidx.paging.PagingData
import com.example.otchallenge.data.entity.Book
import kotlinx.coroutines.CoroutineScope

interface HardcoverFictionListContract {

    interface View {
        fun submitPage(page: PagingData<Book>)
    }

    interface Presenter {
        fun attachView(view: View)
        fun detachView()
        fun subscribeToList(coroutineScope: CoroutineScope)
    }

}