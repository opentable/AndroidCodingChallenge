package com.example.otchallenge.data.repository

import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.data.model.Book
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookService: BooksService,
    private val bookDao: BookDao
) {
    fun getBooks(apiKey: String): Single<List<Book>> {
        return bookService.getBooks(apiKey)
            .map { it.results.books }
            .doOnSuccess { bookDao.insertBooks(it) }
            .onErrorResumeNext {
                bookDao.getBooks().subscribeOn(Schedulers.io())
            }
    }
}