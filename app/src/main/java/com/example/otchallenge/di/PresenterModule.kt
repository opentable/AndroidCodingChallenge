package com.example.otchallenge.di

import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.domain.executor.PostExecutionThread
import com.example.otchallenge.domain.executor.ThreadExecutor
import com.example.otchallenge.domain.usecase.GetBookDetailsUseCaseContract
import com.example.otchallenge.domain.usecase.GetBooksUseCaseContract
import com.example.otchallenge.presentation.presenter.BookDetailPresenter
import com.example.otchallenge.presentation.presenter.BookDetailPresenterContract
import com.example.otchallenge.presentation.presenter.BookListPresenter
import com.example.otchallenge.presentation.presenter.BookListPresenterContract
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
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
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
    ): BookListPresenterContract {
        return BookListPresenter(
            getBooksUseCase,
            compositeDisposable,
            threadExecutor,
            postExecutionThread
        )
    }

    @Provides
    @Singleton
    fun provideBookDetailPresenter(
        getBooksUseCase: GetBookDetailsUseCaseContract,
        compositeDisposable: CompositeDisposable,
        threadExecutor: ThreadExecutor,
        postExecutionThread: PostExecutionThread
    ): BookDetailPresenterContract {
        return BookDetailPresenter(
            getBooksUseCase,
            compositeDisposable,
            threadExecutor,
            postExecutionThread
        )
    }

    @Provides
    @Singleton
    fun provideCompositeDisposable(): CompositeDisposable {
        return CompositeDisposable()
    }
}