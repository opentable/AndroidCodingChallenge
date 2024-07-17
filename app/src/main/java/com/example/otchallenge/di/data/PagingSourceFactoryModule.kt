package com.example.otchallenge.di.data

import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.data.paging.booklist.BookListPagingSourceFactory
import com.example.otchallenge.data.paging.booklist.BookListPagingSourceFactoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class PagingSourceFactoryModule {

    @Binds
    abstract fun provideBookListPagingSourceFactory(
        impl: BookListPagingSourceFactoryImpl
    ): BookListPagingSourceFactory<Int, Book>

}