package com.example.otchallenge.presentation.presenter

interface BookPresenterContract {
    fun loadBooks()
    fun loadBookDetails(id: Int)
    fun clearDisposables()
}