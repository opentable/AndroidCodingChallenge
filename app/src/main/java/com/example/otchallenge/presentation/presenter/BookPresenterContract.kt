package com.example.otchallenge.presentation.presenter

import com.example.otchallenge.presentation.view.BookView

interface BookPresenterContract {
    fun attachView(view: BookView)
    fun detachView()
    fun clearDisposables()
}