package com.example.otchallenge.books.domain

import com.care.sdk.utils.StringResources
import com.example.otchallenge.R
import com.example.otchallenge.books.presentation.BookUiModel

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
    val isbns: String
)

fun Book.toBookUiModel(stringResources: StringResources): BookUiModel {
    return BookUiModel(
        publisher = stringResources.getString(R.string.publisher, publisher),
        description = description,
        title = title,
        image = image,
        imageWidth = imageWidth,
        imageHeight = imageHeight,
        author = stringResources.getString(R.string.author, author),
        isbns = stringResources.getString(R.string.isbn, isbns)
    )
}