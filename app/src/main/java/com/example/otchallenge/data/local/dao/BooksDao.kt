package com.example.otchallenge.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.otchallenge.data.local.entities.BookEntity
import java.time.LocalDate

@Dao
abstract class BooksDao {

    @Query("SELECT * FROM books")
    abstract fun getAll(): List<BookEntity>

    @Query(
        """
            SELECT books.* FROM books 
            INNER JOIN book_list_entries_books
            ON book_list_entries_books.book_isbn13 = books.isbn13
            WHERE
                book_list_entries_books.book_list_id = :bookListId
                AND book_list_entries_books.date = :date
            ORDER BY book_list_entries_books.rank ASC
        """
    )
    abstract fun getForListAndEntryDatePagingSource(
        bookListId: String,
        date: LocalDate
    ): PagingSource<Int, BookEntity>

    @Query(
        """
            SELECT books.* FROM books 
            INNER JOIN book_list_entries_books
            ON book_list_entries_books.book_isbn13 = books.isbn13
            WHERE
                book_list_entries_books.book_list_id = :bookListId
                AND book_list_entries_books.date = (
                    SELECT last_entry_date FROM book_lists WHERE book_lists.id = :bookListId
                ) 
            ORDER BY book_list_entries_books.rank ASC
        """
    )
    abstract fun getCurrentListEntryDatePagingSource(
        bookListId: String,
    ): PagingSource<Int, BookEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(book: BookEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(books: List<BookEntity>)

}