package com.example.otchallenge.presentation.model

data class BookDetailPresentation(
    val id: Int? = -1,
    val rank: Int,
    val description: String,
    val price: String,
    val title: String,
    val author: String,
    val bookImage: String,
    val bookImageWidth: Int,
    val bookImageHeight: Int,
    val amazonProductUrl: String
)