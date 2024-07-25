package com.example.otchallenge.presentation.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.otchallenge.domain.ResultData
import com.example.otchallenge.domain.entity.ItemBook
import com.example.otchallenge.domain.usecase.GetBooksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val booksUseCase: GetBooksUseCase,
) : HomeViewModel, ViewModel() {

    override val viewTypeListState: MutableLiveData<ViewTypeList> =
        MutableLiveData(ViewTypeList.CAROUSEL)
    override val bookList: MutableLiveData<List<ItemBook>> = MutableLiveData()
    override val isLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun toggleViewTypeList(currentState: ViewTypeList) {
        val newState = when (currentState) {
            ViewTypeList.VERTICAL -> ViewTypeList.CAROUSEL
            ViewTypeList.CAROUSEL -> ViewTypeList.VERTICAL
        }
        viewTypeListState.postValue(newState)
    }

    override fun getBooks() {
        viewModelScope.launch {
            isLoading.postValue(true)
            when (val result = booksUseCase.invoke()) {
                is ResultData.Success -> {
                    bookList.postValue(result.data)
                }
                is ResultData.Error -> {
                    Log.e("error", "book case result", result.error)
                }
            }
            isLoading.postValue(false)
        }
    }
}