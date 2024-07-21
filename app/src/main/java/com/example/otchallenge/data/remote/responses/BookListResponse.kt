package com.example.otchallenge.data.remote.responses

import com.squareup.moshi.Json
import java.time.LocalDate
import java.time.ZonedDateTime


data class BookListResponse(
    @Json(name = "num_results")
    val numberOfResults: Int,
    @Json(name = "last_modified")
    val lastModified: ZonedDateTime,
    @Json(name = "results")
    val results: BookListResultResponse
)

data class BookListResultResponse(
    @Json(name = "list_name")
    val listName: String,
    @Json(name = "list_name_encoded")
    val listNameEncoded: String,
    @Json(name = "previous_published_date")
    val previousPublishedDate: LocalDate,
    @Json(name = "normal_list_ends_at")
    val normalListEndsAt: Int,
    @Json(name = "books")
    val books: List<BookItemResponse>
)

data class BookItemResponse(
    @Json(name = "primary_isbn13")
    val primaryIsbn13: String,
    @Json(name = "title")
    val title: String,
    @Json(name = "author")
    val author: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "book_image")
    val bookImage: String,
    @Json(name = "rank")
    val rank: Int
)
