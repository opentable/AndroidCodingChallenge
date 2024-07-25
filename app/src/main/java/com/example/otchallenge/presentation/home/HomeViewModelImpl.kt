package com.example.otchallenge.presentation.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

enum class ViewTypeList {
    VERTICAL,
    CAROUSEL,
}

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
//    val booksUseCase: GetBooksUseCase,
) : ViewModel(), HomeViewModel {
    override val viewTypeListState: MutableLiveData<ViewTypeList> =
        MutableLiveData(ViewTypeList.CAROUSEL)

    override fun toggleViewTypeList(currentState: ViewTypeList) {
        val newState = when (currentState) {
            ViewTypeList.VERTICAL -> ViewTypeList.CAROUSEL
            ViewTypeList.CAROUSEL -> ViewTypeList.VERTICAL
        }
        viewTypeListState.postValue(newState)
    }

    override fun getBooks() {
//        booksUseCase()
    }
}