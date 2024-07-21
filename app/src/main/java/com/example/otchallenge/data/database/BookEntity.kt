package com.example.otchallenge.data.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "books", indices = [Index(value = ["primaryIsbn13"], unique = true)])
data class BookEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val rank: Int,
    val rankLastWeek: Int,
    val weeksOnList: Int,
    val primaryIsbn10: String,
    val primaryIsbn13: String,
    val publisher: String,
    val description: String,
    val price: String,
    val title: String,
    val author: String,
    val contributor: String,
    val bookImage: String,
    val bookImageWidth: Int,
    val bookImageHeight: Int,
    val amazonProductUrl: String,
    val ageGroup: String,
    val bookUri: String
)