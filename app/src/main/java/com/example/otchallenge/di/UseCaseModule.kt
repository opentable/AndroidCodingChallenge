package com.example.otchallenge.di

import com.example.otchallenge.data.BooksRepositoryImpl
import com.example.otchallenge.data.network.BooksService
import com.example.otchallenge.domain.repository.BooksRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object UseCaseModule {

    @Provides
    fun providesBooksRepository(booksService: BooksService): BooksRepository =
        BooksRepositoryImpl(booksService)
}