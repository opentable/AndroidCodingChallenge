package com.example.otchallenge.utils

import com.example.otchallenge.data.network.models.BooksItem
import com.example.otchallenge.data.network.models.BooksResponse
import com.example.otchallenge.data.network.models.Results
import com.example.otchallenge.domain.entity.ItemBook

object SampleData {
    fun booksList(): List<ItemBook> = listOf(
        ItemBook(
            "Book 1",
            "Author 1",
            "Description",
            "bookImageUrl",
            "amazonUrl",
            "1234123"
        )
    )

    fun booksResponse(): BooksResponse = BooksResponse(
        results = Results(
            books = listOf(BooksItem(
                title = "Book 1",
                author = "Author 1",
                description = "Description",
                bookImage = "bookImageUrl",
                amazonProductUrl = "amazonUrl",
                primaryIsbn10 = "1234123"
            ))
        )
    )

    val API_KEY = "FAKE_API"

}
