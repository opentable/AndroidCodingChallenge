package com.example.otchallenge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.otchallenge.data.local.entities.BookListEntryBooksEntity
import java.time.LocalDate

@Dao
abstract class BookListEntryBooksDao {

    @Query("SELECT * FROM book_list_entries_books")
    abstract fun getAll(): List<BookListEntryBooksEntity>

    @Query(
        """
        SELECT COUNT(*)
        FROM book_list_entries_books
        WHERE book_list_id = :bookListId AND date = :date
        """
    )
    abstract fun countForEntry(bookListId: String, date: LocalDate): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(entity: BookListEntryBooksEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(entities: List<BookListEntryBooksEntity>)

    @Query("DELETE FROM book_list_entries_books WHERE book_list_id = :bookListId AND date = :date")
    abstract fun deleteFromEntryBlocking(bookListId: String, date: LocalDate)

}