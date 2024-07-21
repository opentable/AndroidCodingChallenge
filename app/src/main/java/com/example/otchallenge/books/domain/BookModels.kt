package com.example.otchallenge.books.domain

data class BooksData(
    val lastModified: String,
    val books: List<Book>
)

data class Book(
    val publisher: String,
    val description: String,
    val title: String,
    val author: String,
    val image: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val isbns: List<Isbn>
)

data class Isbn(
    val isbn10: String,
    val isbn13: String
)