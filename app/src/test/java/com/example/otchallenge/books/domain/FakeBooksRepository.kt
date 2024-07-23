package com.example.otchallenge.books.domain

import com.example.otchallenge.books.data.NYTimeBook
import com.example.otchallenge.network.NoResponseBodyError
import io.reactivex.Observable
import io.reactivex.Single

class FakeBooksRepository: BooksRepository {

    val returnFalseFromRemoteOffset = -10
    var throwUnknownErrorFromRemote = false
    var throwNoResBodyErrorFromRemote = false

    private val books = mutableListOf<Book>()

    override fun fetchBooksFromRemote(offset: Int): Single<Boolean> {
        if(throwUnknownErrorFromRemote)
            return Single.error(Throwable())
        if (throwNoResBodyErrorFromRemote)
            return Single.error(NoResponseBodyError("Error"))

        return Single.just(offset != returnFalseFromRemoteOffset)
    }

    override fun fetchAllBooks(): Observable<BooksData> {
        if(throwUnknownErrorFromRemote)
            return Observable.error(Throwable())
        if (throwNoResBodyErrorFromRemote)
            return Observable.error(NoResponseBodyError("Error"))

        return Observable.just(
            BooksData(
                lastModified = if (books.isNotEmpty()) "2024-07-17T22:24:46-04:00" else "",
                books
            )
        )
    }

    override fun insertBooks(books: List<NYTimeBook>, lastModified: String) {
        val mappedBooks = books.map {
            it.run {
                Book(
                    publisher = publisher,
                    description = description,
                    title = title,
                    author = author,
                    image = bookImage,
                    imageWidth = bookImageWidth,
                    imageHeight = bookImageHeight,
                    isbns = isbns.joinToString(", ")
                    { isbns ->
                        isbns.convertStringSeparatedByCommas()
                    }
                )
            }
        }
        this.books.addAll(mappedBooks)
    }
}