package com.example.otchallenge.di

import com.example.otchallenge.presentation.screens.books.list.BookListFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun bookListFragment(): BookListFragment

}