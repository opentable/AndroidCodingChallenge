package com.example.otchallenge.books.data

import com.example.otchallenge.books.domain.Book
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NYTimesBooksResponse(
    @Json(name = "status") val status: String,
    @Json(name = "num_results") val numResults: Int,
    @Json(name = "last_modified") val lastModified: String,
    @Json(name = "results") val results: Results
)

@JsonClass(generateAdapter = true)
data class Results(
    @Json(name = "normal_list_ends_at") val normalListEndsAt: Int,
    @Json(name = "books") val books: List<NYTimeBook>
)

@JsonClass(generateAdapter = true)
data class NYTimeBook(
    @Json(name = "publisher") val publisher: String,
    @Json(name = "description") val description: String,
    @Json(name = "title") val title: String,
    @Json(name = "author") val author: String,
    @Json(name = "book_image") val bookImage: String,
    @Json(name = "book_image_width") val bookImageWidth: Int,
    @Json(name = "book_image_height") val bookImageHeight: Int,
    @Json(name = "isbns") val isbns: List<Isbn>,
)

@JsonClass(generateAdapter = true)
data class Isbn(
    @Json(name = "isbn10") val isbn10: String,
    @Json(name = "isbn13") val isbn13: String
)

fun NYTimeBook.toDomainBook() : Book {
    return Book(
        publisher = publisher,
        description = description,
        title = title,
        image = bookImage,
        imageWidth = bookImageWidth,
        imageHeight = bookImageHeight,
        author = author,
        isbns = isbns.map { isbn ->
            com.example.otchallenge.books.domain.Isbn(isbn10 = isbn.isbn10, isbn13 = isbn.isbn13)
        }
    )
}