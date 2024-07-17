package com.example.otchallenge.data.paging.booklist

import androidx.paging.PagingSource
import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.data.remote.BooksApi
import com.example.otchallenge.di.SchedulersModule
import io.reactivex.Scheduler
import javax.inject.Inject
import javax.inject.Named

interface BookListPagingSourceFactory<K: Any, T: Any> {
    operator fun invoke(listName: String): PagingSource<K, T>
}

class BookListPagingSourceFactoryImpl @Inject constructor(
    private val bookListApi: BooksApi,
    @Named(SchedulersModule.IO)
    private val ioScheduler: Scheduler
): BookListPagingSourceFactory<Int, Book> {

    override fun invoke(listName: String): PagingSource<Int, Book> {
        return BookListPagingSource(
            listName = listName,
            booksApi = bookListApi,
            ioScheduler = ioScheduler
        )
    }

}
