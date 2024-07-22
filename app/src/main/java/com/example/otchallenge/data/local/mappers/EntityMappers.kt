package com.example.otchallenge.data.local.mappers

import com.example.otchallenge.data.local.entities.BookEntity
import com.example.otchallenge.data.model.Book

fun BookEntity.toBook(): Book {
    return Book(
        isbn13 = isbn13,
        title = title,
        author = author,
        description = description,
        imageUrl = imageUrl
    )
}