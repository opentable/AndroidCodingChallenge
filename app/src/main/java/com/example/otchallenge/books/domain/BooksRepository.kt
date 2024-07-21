package com.example.otchallenge.books.domain

import io.reactivex.Single

interface BooksRepository {
    fun fetchBooks(offset: Int): Single<BooksData>
}