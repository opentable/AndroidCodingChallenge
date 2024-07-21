package com.example.otchallenge.domain.usecase

import com.example.otchallenge.domain.model.Book
import com.example.otchallenge.domain.model.BookSummary
import io.reactivex.Single

interface BookLoader {
    fun loadBooks(): Single<List<BookSummary>>
    fun loadBookDetails(id: Int): Single<Book>
}