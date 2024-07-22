package com.example.otchallenge.data.remote.mappers

import com.example.otchallenge.data.model.Book
import com.example.otchallenge.data.remote.responses.BookListResponse

fun BookListResponse.toBookEntityList(): List<Book> {
    return results.books.map {
        Book(
            isbn13 = it.primaryIsbn13,
            title = it.title,
            author = it.author,
            description = it.description,
            imageUrl = it.bookImage
        )
    }
}