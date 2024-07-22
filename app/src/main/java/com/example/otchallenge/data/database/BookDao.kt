package com.example.otchallenge.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface BookDao {
    @Query("SELECT id, title, description, bookImage FROM books")
    fun getBooks(): Single<List<BookSummaryEntity>>

    @Query("SELECT * FROM books WHERE id = :bookId")
    fun getBookById(bookId: Int): Single<BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooks(books: List<BookEntity>): Completable
}