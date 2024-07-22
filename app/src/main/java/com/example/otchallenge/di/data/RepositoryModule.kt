package com.example.otchallenge.di.data

import com.example.otchallenge.data.repository.BookRepository
import com.example.otchallenge.data.repository.BookRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindBookRepository(
        impl: BookRepositoryImpl
    ): BookRepository

}