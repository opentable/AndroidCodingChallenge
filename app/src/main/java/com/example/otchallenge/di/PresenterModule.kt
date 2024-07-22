package com.example.otchallenge.di

import com.example.otchallenge.presentation.screens.books.list.BookListContract
import com.example.otchallenge.presentation.screens.books.list.BookListPresenter
import dagger.Binds
import dagger.Module

@Module
abstract class PresenterModule {

    @Binds
    abstract fun bindBookListPresenter(
        impl: BookListPresenter
    ): BookListContract.Presenter

}