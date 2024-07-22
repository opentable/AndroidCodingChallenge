package com.example.otchallenge.books.presentation

data class BookUiModel(
    val publisher: String,
    val description: String,
    val title: String,
    val author: String,
    val image: String,
    val imageWidth: Int,
    val imageHeight: Int,
    val isbns: String
)