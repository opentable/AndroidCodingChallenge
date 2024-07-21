package com.example.otchallenge.presentation.view

import com.example.otchallenge.presentation.model.BookDetailPresentation

interface BookDetailView : BookView {
    fun showBookDetails(book: BookDetailPresentation)
}