package com.example.otchallenge.di.data

import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.data.paging.HardCoverFictionPagingSourceFactory
import com.example.otchallenge.data.paging.PagingSourceFactory
import dagger.Binds
import dagger.Module

@Module
abstract class PagingModule {

    @Binds
    abstract fun provideHardcoverFictionPagingSourceFactory(
        impl: HardCoverFictionPagingSourceFactory
    ): PagingSourceFactory<Int, Book>

}