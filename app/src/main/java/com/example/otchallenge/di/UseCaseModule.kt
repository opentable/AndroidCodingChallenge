package com.example.otchallenge.di

import com.example.otchallenge.domain.repository.BookDetailsRepository
import com.example.otchallenge.domain.repository.BookListRepository
import com.example.otchallenge.domain.executor.PostExecutionThread
import com.example.otchallenge.domain.executor.ThreadExecutor
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
    fun provideGetBooksUseCase(
        bookRepository: BookListRepository, threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
    ): GetBooksUseCaseContract {
        return GetBooksUseCase(bookRepository, threadExecutor, postExecutionThread)
    }

    @Provides
    @Singleton
    fun provideGetBookDetailsUseCase(
        bookRepository: BookDetailsRepository,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
    ): GetBookDetailsUseCaseContract {
        return GetBookDetailsUseCase(bookRepository, threadExecutor, postExecutionThread)
    }
}