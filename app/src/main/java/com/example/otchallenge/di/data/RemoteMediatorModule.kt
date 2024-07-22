package com.example.otchallenge.di.data

import com.example.otchallenge.data.local.dao.composite.BookListPaginationDao
import com.example.otchallenge.data.local.dao.composite.BookListPaginationDaoImpl
import com.example.otchallenge.data.paging.booklist.BookListMediator
import com.example.otchallenge.data.paging.booklist.BookListMediatorImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RemoteMediatorModule {

    @Binds
    abstract fun bindBookListRemoteMediator(
        impl: BookListMediatorImpl
    ): BookListMediator

    @Binds
    abstract fun bindBookListPaginationDao(
        impl: BookListPaginationDaoImpl
    ): BookListPaginationDao
}