package com.example.otchallenge.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import java.time.LocalDate

@Entity(
    tableName = "book_list_remote_keys",
    primaryKeys = [
        "book_list_id",
        "date"
    ]
)
data class BookListRemoteKeyEntity (
    @ColumnInfo("book_list_id")
    val bookListId: String,
    @ColumnInfo("date")
    val date: LocalDate,
    @ColumnInfo("next_key")
    val nextKey: Int?
)