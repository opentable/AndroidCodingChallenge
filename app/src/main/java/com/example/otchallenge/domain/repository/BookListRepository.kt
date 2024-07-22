package com.example.otchallenge.domain.repository

import com.example.otchallenge.domain.model.BookSummary
import io.reactivex.Single

interface BookListRepository {
    fun getBooks(): Single<List<BookSummary>>
}