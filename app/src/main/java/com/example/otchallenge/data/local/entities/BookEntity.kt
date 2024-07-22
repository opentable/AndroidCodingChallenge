package com.example.otchallenge.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BookEntity (
    @PrimaryKey
    @ColumnInfo(name = "isbn13")
    val isbn13: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "author")
    val author: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String
)