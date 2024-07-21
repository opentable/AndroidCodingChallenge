package com.example.otchallenge.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.otchallenge.data.local.entities.BookListRemoteKeyEntity
import io.reactivex.Maybe
import java.time.LocalDate

@Dao
abstract class BookListRemoteKeysDao {

    @Query("SELECT * FROM book_list_remote_keys")
    abstract fun getAll(): List<BookListRemoteKeyEntity>

    @Query(
        """
            SELECT * FROM book_list_remote_keys
            WHERE book_list_id = :bookListId AND date = :date
        """
    )
    abstract fun findMaybe(bookListId: String, date: LocalDate): Maybe<BookListRemoteKeyEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun upsert(entity: BookListRemoteKeyEntity)

}