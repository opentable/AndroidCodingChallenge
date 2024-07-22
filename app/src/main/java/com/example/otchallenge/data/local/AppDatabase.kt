package com.example.otchallenge.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.otchallenge.data.local.converters.InstantConverter
import com.example.otchallenge.data.local.converters.LocalDateConverter
import com.example.otchallenge.data.local.dao.BookListEntriesDao
import com.example.otchallenge.data.local.dao.BookListEntryBooksDao
import com.example.otchallenge.data.local.dao.BookListRemoteKeysDao
import com.example.otchallenge.data.local.dao.BookListsDao
import com.example.otchallenge.data.local.dao.BooksDao
import com.example.otchallenge.data.local.entities.BookEntity
import com.example.otchallenge.data.local.entities.BookListEntity
import com.example.otchallenge.data.local.entities.BookListEntryBooksEntity
import com.example.otchallenge.data.local.entities.BookListEntryEntity
import com.example.otchallenge.data.local.entities.BookListRemoteKeyEntity

@Database(
    entities = [
        BookEntity::class,
        BookListEntity::class,
        BookListEntryEntity::class,
        BookListEntryBooksEntity::class,
        BookListRemoteKeyEntity::class
    ],
    version = 1
)
@TypeConverters(
    LocalDateConverter::class,
    InstantConverter::class
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun booksDao(): BooksDao
    abstract fun bookListsDao(): BookListsDao
    abstract fun bookListEntriesDao(): BookListEntriesDao
    abstract fun bookListEntryBooksDao(): BookListEntryBooksDao
    abstract fun bookListRemoteKeysDao(): BookListRemoteKeysDao

}