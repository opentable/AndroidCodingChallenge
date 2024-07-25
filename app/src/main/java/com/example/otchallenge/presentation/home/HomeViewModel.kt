package com.example.otchallenge.presentation.home

import androidx.lifecycle.LiveData
import com.example.otchallenge.domain.entity.ItemBook

interface HomeViewModel {

    val viewTypeListState: LiveData<ViewTypeList>
    val bookList: LiveData<List<ItemBook>>
    val isLoading: LiveData<Boolean>

    fun toggleViewTypeList(currentState: ViewTypeList)

    fun getBooks()
}

enum class ViewTypeList {
    VERTICAL,
    CAROUSEL,
}