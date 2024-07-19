package com.example.otchallenge.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.otchallenge.data.model.Book
import io.reactivex.Single

@Dao
interface BookDao {
    @Query("SELECT * FROM books")
    fun getBooks(): Single<List<Book>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBooks(books: List<Book>)
}