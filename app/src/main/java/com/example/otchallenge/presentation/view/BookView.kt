package com.example.otchallenge.presentation.view

import com.example.otchallenge.presentation.model.BookDetailPresentation
import com.example.otchallenge.presentation.model.BookPresentation

interface BookView {
    fun showBooks(books: List<BookPresentation>)
    fun showBookDetails(book: BookDetailPresentation)
    fun showError(error: String)
}