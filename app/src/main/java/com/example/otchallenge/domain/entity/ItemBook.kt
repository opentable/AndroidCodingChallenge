package com.example.otchallenge.domain.entity

data class ItemBook(
    val title: String,
    val author: String,
    val description: String,
    val bookImageUrl: String,
    val amazonBuyUrl: String,
    val isbn: String,
)
