package com.example.otchallenge.di

import com.example.otchallenge.data.repository.BookRepository
import com.example.otchallenge.domain.usecase.GetBooksUseCase
import com.example.otchallenge.domain.usecase.GetBooksUseCaseContract
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetBooksUseCase(bookRepository: BookRepository): GetBooksUseCaseContract {
        return GetBooksUseCase(bookRepository)
    }
}