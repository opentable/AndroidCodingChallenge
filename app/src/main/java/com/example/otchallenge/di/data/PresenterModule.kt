package com.example.otchallenge.di.data

import com.example.otchallenge.presentation.BookListPresenter
import com.example.otchallenge.presentation.HardcoverFictionListContract
import dagger.Binds
import dagger.Module

@Module
abstract class PresenterModule {

    @Binds
    abstract fun provideHardcoverFictionListPresenter(
        impl: BookListPresenter
    ): HardcoverFictionListContract.Presenter

}