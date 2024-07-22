package com.example.otchallenge.data.paging.booklist.mappers

import com.example.otchallenge.data.local.entities.BookEntity
import com.example.otchallenge.data.local.entities.BookListEntity
import com.example.otchallenge.data.local.entities.BookListEntryBooksEntity
import com.example.otchallenge.data.local.entities.BookListEntryEntity
import com.example.otchallenge.data.remote.responses.BookListResponse
import java.time.ZoneId
import java.time.ZoneOffset

fun BookListResponse.getBookListEntity(): BookListEntity {
    return BookListEntity(
        id = results.listNameEncoded,
        name = results.listName,
        lastEntryDate = results.previousPublishedDate
    )
}

fun BookListResponse.getBookListEntryEntity(): BookListEntryEntity {
    return BookListEntryEntity(
        bookListId = results.listNameEncoded,
        date = results.previousPublishedDate,
        size = results.normalListEndsAt,
        lastModified = lastModified.withZoneSameInstant(ZoneId.of(ZoneOffset.UTC.id)).toInstant()
    )
}

fun BookListResponse.getBookListEntryBookEntities(): List<BookListEntryBooksEntity> {
    return results.books.map { book ->
        BookListEntryBooksEntity(
            bookListId = results.listNameEncoded,
            date = results.previousPublishedDate,
            bookIsbn13 = book.primaryIsbn13,
            rank = book.rank
        )
    }
}

fun BookListResponse.getBookEntities(): List<BookEntity> {
    return results.books.map { book ->
        BookEntity(
            isbn13 = book.primaryIsbn13,
            title = book.title,
            author = book.author,
            description = book.description,
            imageUrl = book.bookImage
        )
    }
}
