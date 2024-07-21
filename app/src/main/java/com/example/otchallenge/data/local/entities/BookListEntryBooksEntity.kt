package com.example.otchallenge.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.LocalDate

@Entity(
    tableName = "book_list_entries_books",
    primaryKeys = [
        "book_list_id",
        "date",
        "book_isbn13"
    ]
)
data class BookListEntryBooksEntity(
    @ColumnInfo(name = "book_list_id")
    val bookListId: String,
    @ColumnInfo(name = "date")
    val date: LocalDate,
    @ColumnInfo(name = "book_isbn13")
    val bookIsbn13: String,
    @ColumnInfo(name = "rank")
    val rank: Int
)
