package com.example.otchallenge.di

import com.example.otchallenge.domain.repository.BooksRepository
import com.example.otchallenge.domain.usecase.GetBooksUseCase
import com.example.otchallenge.domain.usecase.GetBooksUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
object ViewModelModule {

    @Provides
    fun provideGetBooksUseCase(booksRepository: BooksRepository): GetBooksUseCase =
        GetBooksUseCaseImpl(booksRepository)


}