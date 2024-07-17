package com.example.otchallenge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.observable
import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.data.paging.booklist.BookListPagingSourceFactory
import com.example.otchallenge.di.data.PagingConfigModule
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Named

interface BookRepository {

    fun getBookList(listName: String): Observable<PagingData<Book>>

}

class BookRepositoryImpl @Inject constructor(
    @Named(PagingConfigModule.BookList)
    private val bookListPagingConfig: PagingConfig,
    private val bookListPagingSourceFactory: BookListPagingSourceFactory<Int, Book>
) : BookRepository {

    override fun getBookList(listName: String): Observable<PagingData<Book>> {
        return Pager(
            config = bookListPagingConfig,
            initialKey = 0,
            pagingSourceFactory = {
                bookListPagingSourceFactory(
                    listName = listName
                )
            }
        ).observable
    }

}