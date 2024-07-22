package com.example.otchallenge.data.paging

import com.example.otchallenge.data.remote.responses.BookItemResponse
import com.example.otchallenge.data.remote.responses.BookListResponse
import com.example.otchallenge.data.remote.responses.BookListResultResponse
import java.time.LocalDate
import java.time.ZonedDateTime

fun generateResponse(
    listId: String,
    books: Int = 15,
    offset: Int = 0,
    totalCount: Int = books,
    date: LocalDate = LocalDate.now(),
    lastModified: ZonedDateTime = ZonedDateTime.now()
): BookListResponse {
    return BookListResponse(
        numberOfResults = books,
        lastModified = lastModified,
        results = BookListResultResponse(
            listName = "Test List",
            listNameEncoded = listId,
            previousPublishedDate = date,
            normalListEndsAt = totalCount,
            books = List(books) {
                val n = offset + it
                BookItemResponse(
                    primaryIsbn13 = "$n",
                    title = "title$n",
                    author = "author$n",
                    description = "desc$n",
                    bookImage = "image$n",
                    rank = n
                )
            }
        )
    )
}
