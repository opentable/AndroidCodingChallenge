package com.example.otchallenge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.otchallenge.data.local.entities.BookListEntryEntity

@Dao
abstract class BookListEntriesDao {

    @Query("SELECT * FROM book_list_entries")
    abstract fun getAll(): List<BookListEntryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(entity: BookListEntryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(entities: List<BookListEntryEntity>)

}