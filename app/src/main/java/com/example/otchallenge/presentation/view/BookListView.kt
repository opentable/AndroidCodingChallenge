package com.example.otchallenge.presentation.view

import com.example.otchallenge.presentation.model.BookPresentation

interface BookListView : BookView {
    fun showBooks(books: List<BookPresentation>)
}