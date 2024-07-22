package com.example.otchallenge.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.map
import androidx.paging.rxjava2.observable
import com.example.otchallenge.data.local.AppDatabase
import com.example.otchallenge.data.local.entities.BookEntity
import com.example.otchallenge.data.local.mappers.toBook
import com.example.otchallenge.data.model.Book
import com.example.otchallenge.data.model.BookList
import com.example.otchallenge.data.paging.booklist.BookListMediator
import com.example.otchallenge.data.repository.BookRepository.Companion.DEFAULT_BOOK_LIST
import com.example.otchallenge.di.data.PagingConfigModule
import io.reactivex.Maybe
import io.reactivex.Observable
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

interface BookRepository {

    fun getBookList(
        bookListId: String?
    ): Maybe<BookList>

    fun getBookListPages(
        bookListId: String,
        date: LocalDate? = null
    ): Observable<PagingData<Book>>

    companion object {
        val DEFAULT_BOOK_LIST = BookList(
            id = "hardcover-fiction",
            name = "Hardcover Fiction"
        )
    }
}

class BookRepositoryImpl @Inject constructor(
    @Named(PagingConfigModule.BookList)
    private val bookListPagingConfig: PagingConfig,
    private val bookListMediator: BookListMediator,
    private val appDatabase: AppDatabase
) : BookRepository {

    // Book list storage not supported yet. We return always the same book list
    override fun getBookList(bookListId: String?): Maybe<BookList> {
        return Maybe.just(DEFAULT_BOOK_LIST)
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getBookListPages(
        bookListId: String,
        date: LocalDate?
    ): Observable<PagingData<Book>> {
        return getMediatorOptions(
            bookListId = bookListId,
            date = date
        ).let { options ->
            Pager(
                config = bookListPagingConfig,
                remoteMediator = bookListMediator.withOptions(options),
                pagingSourceFactory = { options.getPagingSource() }
            ).observable.map { pagingData ->
                pagingData.map {
                    it.toBook()
                }
            }
        }
    }

    private fun getMediatorOptions(
        bookListId: String,
        date: LocalDate?
    ): BookListMediator.Options {
        return if (date == null) {
            BookListMediator.Options.LoadCurrent(bookListId)
        } else {
            BookListMediator.Options.LoadDate(bookListId, date)
        }
    }

    private fun BookListMediator.Options.getPagingSource(): PagingSource<Int, BookEntity> {
        return appDatabase.booksDao().let { dao ->
            when (this) {
                is BookListMediator.Options.LoadCurrent -> {
                    dao.getCurrentListEntryDatePagingSource(
                        bookListId = bookListId
                    )
                }
                is BookListMediator.Options.LoadDate -> {
                    dao.getForListAndEntryDatePagingSource(
                        bookListId = bookListId,
                        date = date
                    )
                }
            }
        }
    }

}