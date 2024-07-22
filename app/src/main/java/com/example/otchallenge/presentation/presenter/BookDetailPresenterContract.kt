package com.example.otchallenge.presentation.presenter

interface BookDetailPresenterContract : BookPresenterContract {
    fun loadBookDetails(id: Int)
}