package com.example.otchallenge.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.otchallenge.data.model.BookApi

@Database(entities = [BookEntity::class], version = 1)
abstract class BookDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
}