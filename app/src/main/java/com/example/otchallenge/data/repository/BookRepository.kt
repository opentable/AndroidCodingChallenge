package com.example.otchallenge.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.rxjava2.observable
import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.data.paging.PagingSourceFactory
import io.reactivex.Observable
import javax.inject.Inject

interface BookRepository {

    fun getHardcoverFictionList(): Observable<PagingData<Book>>

}

class BookRepositoryImpl @Inject constructor(
    private val hardcoverFictionPagingSourceFactory: PagingSourceFactory<Int, Book>
) : BookRepository {

    override fun getHardcoverFictionList(): Observable<PagingData<Book>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                prefetchDistance = 5,
                initialLoadSize = 20
            ),
            initialKey = 0,
            pagingSourceFactory = {
                hardcoverFictionPagingSourceFactory()
            }
        ).observable
    }

}