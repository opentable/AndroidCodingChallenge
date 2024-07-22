package com.example.otchallenge.data.di

import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.data.repository.BookDetailsRepositoryImpl
import com.example.otchallenge.data.repository.BookListRepositoryImpl
import com.example.otchallenge.domain.repository.BookDetailsRepository
import com.example.otchallenge.domain.repository.BookListRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideBookListRepository(
        bookService: BooksService,
        bookDao: BookDao
    ): BookListRepository {
        return BookListRepositoryImpl(bookService, bookDao)
    }

    @Provides
    @Singleton
    fun provideBookDetailsRepository(
        bookDao: BookDao
    ): BookDetailsRepository {
        return BookDetailsRepositoryImpl(bookDao)
    }
}