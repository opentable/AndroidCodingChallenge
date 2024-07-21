package com.example.otchallenge.di

import com.example.otchallenge.data.repository.BookDetailsRepository
import com.example.otchallenge.data.repository.BookListRepository
import com.example.otchallenge.domain.usecase.GetBookDetailsUseCase
import com.example.otchallenge.domain.usecase.GetBookDetailsUseCaseContract
import com.example.otchallenge.domain.usecase.GetBooksUseCase
import com.example.otchallenge.domain.usecase.GetBooksUseCaseContract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetBooksUseCase(bookRepository: BookListRepository): GetBooksUseCaseContract {
        return GetBooksUseCase(bookRepository)
    }

    @Provides
    @Singleton
    fun provideGetBookDetailsUseCase(bookRepository: BookDetailsRepository): GetBookDetailsUseCaseContract {
        return GetBookDetailsUseCase(bookRepository)
    }
}