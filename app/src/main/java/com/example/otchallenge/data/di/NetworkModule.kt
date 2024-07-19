package com.example.otchallenge.data.di

import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.utils.NetworkInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(networkInterceptor: NetworkInterceptor): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(networkInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.nytimes.com/svc/books/v3/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBookService(retrofit: Retrofit): BooksService {
        return retrofit.create(BooksService::class.java)
    }
}