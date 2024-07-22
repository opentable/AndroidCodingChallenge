package com.example.otchallenge.data.local.dao.composite

import com.example.otchallenge.data.local.AppDatabase
import com.example.otchallenge.data.local.entities.BookEntity
import com.example.otchallenge.data.local.entities.BookListEntity
import com.example.otchallenge.data.local.entities.BookListEntryBooksEntity
import com.example.otchallenge.data.local.entities.BookListEntryEntity
import com.example.otchallenge.data.local.entities.BookListRemoteKeyEntity
import com.example.otchallenge.data.local.extensions.runInTransactionMaybe
import io.reactivex.Maybe
import javax.inject.Inject

interface BookListPaginationDao {

    fun upsertListEntry(
        bookList: BookListEntity,
        entry: BookListEntryEntity,
        entryBooks: List<BookListEntryBooksEntity>,
        books: List<BookEntity>,
        nextKeyFactory: (pageSize: Int, totalCount: Int) -> Int?
    ): Maybe<Int>

    fun appendEntryBooks(
        entry: BookListEntryEntity,
        entryBooks: List<BookListEntryBooksEntity>,
        books: List<BookEntity>,
        nextKeyFactory: (pageSize: Int, totalCount: Int) -> Int?
    ): Maybe<Int>

}

class BookListPaginationDaoImpl @Inject constructor(
    private val database: AppDatabase
) : BookListPaginationDao {

    private val booksDao = database.booksDao()
    private val bookListsDao = database.bookListsDao()
    private val bookListEntriesDao = database.bookListEntriesDao()
    private val bookListEntryBooksDao = database.bookListEntryBooksDao()
    private val bookListRemoteKeysDao = database.bookListRemoteKeysDao()

    override fun upsertListEntry(
        bookList: BookListEntity,
        entry: BookListEntryEntity,
        entryBooks: List<BookListEntryBooksEntity>,
        books: List<BookEntity>,
        nextKeyFactory: (pageSize: Int, totalCount: Int) -> Int?,
    ): Maybe<Int> {
        return database.runInTransactionMaybe {
            bookListsDao.upsert(entity = bookList)

            // Remove previous entries for this list
            bookListEntryBooksDao.deleteFromEntryBlocking(
                bookListId = entry.bookListId,
                date = entry.date
            )

            // Upsert entry
            bookListEntriesDao.upsert(entry)

            // Upsert books
            booksDao.upsert(books = books)

            // Insert new entry books
            bookListEntryBooksDao.upsert(entities = entryBooks)

            // Get the total count for this entry
            // We pass this value as a parameter for getting our next key
            val totalCount = bookListEntryBooksDao.countForEntry(
                bookListId = entry.bookListId,
                date = entry.date
            )


            return@runInTransactionMaybe nextKeyFactory(
                books.size,
                totalCount
            ).also { nextKey ->
                bookListRemoteKeysDao.upsert(
                    entity = BookListRemoteKeyEntity(
                        bookListId = entry.bookListId,
                        date = entry.date,
                        nextKey = nextKey
                    )
                )
            }
        }
    }

    override fun appendEntryBooks(
        entry: BookListEntryEntity,
        entryBooks: List<BookListEntryBooksEntity>,
        books: List<BookEntity>,
        nextKeyFactory: (pageSize: Int, totalCount: Int) -> Int?
    ): Maybe<Int> {
        return database.runInTransactionMaybe {

            // Upsert books
            booksDao.upsert(books = books)

            // Insert new entry books
            bookListEntryBooksDao.upsert(entities = entryBooks)

            val totalCount = bookListEntryBooksDao.countForEntry(
                bookListId = entry.bookListId,
                date = entry.date
            )

            return@runInTransactionMaybe nextKeyFactory(
                books.size,
                totalCount
            ).also { nextKey ->
                bookListRemoteKeysDao.upsert(
                    entity = BookListRemoteKeyEntity(
                        bookListId = entry.bookListId,
                        date = entry.date,
                        nextKey = nextKey
                    )
                )
            }
        }
    }

}