package com.example.otchallenge.presentation.home

import androidx.lifecycle.LiveData

interface HomeViewModel {

    val viewTypeListState: LiveData<ViewTypeList>

    fun toggleViewTypeList(currentState: ViewTypeList)

    fun getBooks()
}