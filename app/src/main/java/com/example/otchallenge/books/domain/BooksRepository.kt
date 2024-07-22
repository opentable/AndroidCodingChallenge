package com.example.otchallenge.books.domain

import com.example.otchallenge.books.data.NYTimeBook
import io.reactivex.Observable
import io.reactivex.Single

interface BooksRepository {
    fun fetchBooks(offset: Int): Single<Boolean>
    fun fetchAllBooks(): Observable<BooksData>
    fun insertBooks(books: List<NYTimeBook>, lastModified: String)
}