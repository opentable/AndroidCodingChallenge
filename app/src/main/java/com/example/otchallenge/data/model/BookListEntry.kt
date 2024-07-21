package com.example.otchallenge.data.model

import java.time.Instant
import java.time.LocalDate

data class BookListEntry(
    val bookList: BookList,
    val books: List<Book>,
    val date: LocalDate,
    val lastModified: Instant
)
