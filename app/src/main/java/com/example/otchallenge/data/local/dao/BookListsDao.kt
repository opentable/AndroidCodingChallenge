package com.example.otchallenge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.otchallenge.data.local.entities.BookListEntity
import io.reactivex.Maybe

@Dao
abstract class BookListsDao {

    @Query("SELECT * FROM book_lists")
    abstract fun getAll(): List<BookListEntity>

    @Query("SELECT * FROM book_lists WHERE id = :id")
    abstract fun getByIdMaybe(id: String): Maybe<BookListEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(entity: BookListEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(entities: List<BookListEntity>)

}