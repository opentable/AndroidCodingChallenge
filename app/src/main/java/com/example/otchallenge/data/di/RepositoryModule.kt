package com.example.otchallenge.data.di

import com.example.otchallenge.data.repository.BookDetailsRepository
import com.example.otchallenge.data.repository.BookListRepository
import com.example.otchallenge.domain.repository.BookDetailsRepositoryContract
import com.example.otchallenge.domain.repository.BookListRepositoryContract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideBookListRepository(bookListRepository: BookListRepository): BookListRepositoryContract {
        return bookListRepository
    }

    @Provides
    @Singleton
    fun provideBookDetailsRepository(bookDetailsRepository: BookDetailsRepository): BookDetailsRepositoryContract {
        return bookDetailsRepository
    }
}