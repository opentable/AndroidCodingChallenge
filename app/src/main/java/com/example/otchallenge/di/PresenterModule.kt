package com.example.otchallenge.di

import com.example.otchallenge.domain.usecase.GetBooksUseCaseContract
import com.example.otchallenge.presentation.presenter.BookPresenter
import com.example.otchallenge.presentation.presenter.BookPresenterContract
import com.example.otchallenge.utils.ConnectivityChecker
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
    fun provideBookPresenter(
        getBooksUseCase: GetBooksUseCaseContract,
        connectivityChecker: ConnectivityChecker,
        compositeDisposable: CompositeDisposable,
        @Named("io") ioScheduler: Scheduler,
    ): BookPresenterContract {
        return BookPresenter(
            getBooksUseCase,
            connectivityChecker,
            compositeDisposable,
            ioScheduler
        )
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