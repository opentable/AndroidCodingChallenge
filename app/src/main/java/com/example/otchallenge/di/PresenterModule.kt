package com.example.otchallenge.di

import com.example.otchallenge.domain.usecase.GetBookDetailsUseCaseContract
import com.example.otchallenge.domain.usecase.GetBooksUseCaseContract
import com.example.otchallenge.presentation.presenter.BookDetailPresenter
import com.example.otchallenge.presentation.presenter.BookDetailPresenterContract
import com.example.otchallenge.presentation.presenter.BookListPresenter
import com.example.otchallenge.presentation.presenter.BookListPresenterContract
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Named
import javax.inject.Singleton

@Module
class PresenterModule {

    @Provides
    @Singleton
    fun provideBookListPresenter(
        getBooksUseCase: GetBooksUseCaseContract,
        compositeDisposable: CompositeDisposable,
        @Named("io") ioScheduler: Scheduler
    ): BookListPresenterContract {
        return BookListPresenter(getBooksUseCase, compositeDisposable, ioScheduler)
    }

    @Provides
    @Singleton
    fun provideBookDetailPresenter(
        getBooksUseCase: GetBookDetailsUseCaseContract,
        compositeDisposable: CompositeDisposable,
        @Named("io") ioScheduler: Scheduler
    ): BookDetailPresenterContract {
        return BookDetailPresenter(getBooksUseCase, compositeDisposable, ioScheduler)
    }

    @Provides
    @Singleton
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }

    @Provides
    @Singleton
    @Named("io")
    fun provideIoScheduler(): Scheduler {
        return Schedulers.io()
    }
}