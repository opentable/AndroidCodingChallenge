package com.example.otchallenge.di.data

import com.example.otchallenge.data.remote.BooksApi
import dagger.Module
import dagger.Provides
import io.mockk.mockk
import javax.inject.Singleton

@Module
class TestRemoteModule {

    @Provides
    @Singleton
    fun provideBooksApi(): BooksApi {
        return mockk<BooksApi>()
    }
}