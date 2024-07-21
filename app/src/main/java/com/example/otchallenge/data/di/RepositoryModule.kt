package com.example.otchallenge.data.di

import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.data.repository.BookRepository
import com.example.otchallenge.domain.usecase.BookLoader
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideBookRepository(
        bookService: BooksService,
        bookDao: BookDao
    ): BookLoader {
        return BookRepository(bookService, bookDao)
    }
}