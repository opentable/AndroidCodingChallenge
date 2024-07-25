package com.example.otchallenge.data.network.di

import com.example.otchallenge.BuildConfig
import com.example.otchallenge.data.network.BooksService
import com.example.otchallenge.data.network.utils.createRetrofit
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RetrofitModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = createRetrofit("https://api.nytimes.com/", BuildConfig.DEBUG)

    @Provides
    fun provideBooksService(retrofit: Retrofit): BooksService = retrofit.create()
}