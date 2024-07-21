package com.example.otchallenge.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.Instant
import java.time.LocalDate

@Entity(
    tableName = "book_list_entries",
    primaryKeys = [
        "book_list_id",
        "date"
    ]
)
data class BookListEntryEntity(
    @ColumnInfo(name = "book_list_id")
    val bookListId: String,
    @ColumnInfo(name = "date")
    val date: LocalDate,
    @ColumnInfo(name = "size")
    val size: Int,
    @ColumnInfo(name = "last_modified")
    val lastModified: Instant
)
