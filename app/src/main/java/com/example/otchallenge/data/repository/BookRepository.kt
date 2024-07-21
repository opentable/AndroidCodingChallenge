package com.example.otchallenge.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava2.observable
import com.example.otchallenge.data.local.AppDatabase
import com.example.otchallenge.data.model.Book
import com.example.otchallenge.data.paging.booklist.BookListMediator
import com.example.otchallenge.di.data.PagingConfigModule
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

interface BookRepository {

    fun getBookList(
        options: BookListMediator.Options
    ): Observable<PagingData<Book>>

}

class BookRepositoryImpl @Inject constructor(
    @Named(PagingConfigModule.BookList)
    private val bookListPagingConfig: PagingConfig,
    private val bookListMediator: BookListMediator,
    private val appDatabase: AppDatabase
) : BookRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getBookList(
        options: BookListMediator.Options
    ): Observable<PagingData<Book>> {
        return Pager(
            config = bookListPagingConfig,
            initialKey = 0,
            remoteMediator = bookListMediator.withOptions(options),
            pagingSourceFactory = {

                val dao = appDatabase.booksDao()

                when (options) {
                    is BookListMediator.Options.LoadCurrent -> {
                        dao.getCurrentListEntryDatePagingSource(
                            bookListId = options.bookListId
                        )
                    }
                    is BookListMediator.Options.LoadDate -> {
                        dao.getForListAndEntryDatePagingSource(
                            bookListId = options.bookListId,
                            date = options.date
                        )
                    }
                }
            }
        ).observable.map { pagingData ->
            pagingData.map { entity ->
                Book(
                    isbn13 = entity.isbn13,
                    title = entity.title,
                    author = entity.author,
                    description = entity.description,
                    imageUrl = entity.imageUrl
                )
            }
        }
    }

}