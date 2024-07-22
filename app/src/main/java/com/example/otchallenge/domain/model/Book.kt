package com.example.otchallenge.domain.model

data class Book(
    val id: Int? = null,
    val rank: Int,
    val rankLastWeek: Int,
    val weeksOnList: Int,
    val primaryIsbn10: String,
    val primaryIsbn13: String,
    val publisher: String,
    val description: String,
    val price: String,
    val title: String,
    val author: String,
    val contributor: String,
    val bookImage: String,
    val bookImageWidth: Int,
    val bookImageHeight: Int,
    val amazonProductUrl: String,
    val ageGroup: String,
    val bookUri: String?
)