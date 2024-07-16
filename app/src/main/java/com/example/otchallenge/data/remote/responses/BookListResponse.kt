package com.example.otchallenge.data.remote.responses

import com.squareup.moshi.Json


data class BookListResponse(
    @Json(name = "num_results")
    val numberOfResults: Int,
    @Json(name = "results")
    val results: BookListResultResponse
)

data class BookListResultResponse(
    @Json(name = "list_name")
    val listName: String,
    @Json(name = "books")
    val books: List<BookItemResponse>
)

data class BookItemResponse(
    @Json(name = "title")
    val title: String,
    @Json(name = "author")
    val author: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "book_image")
    val bookImage: String
)
