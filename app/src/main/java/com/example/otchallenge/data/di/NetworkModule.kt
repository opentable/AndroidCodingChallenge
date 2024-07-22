package com.example.otchallenge.data.di

import com.example.otchallenge.BuildConfig
import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.utils.NetworkInterceptor
import com.example.otchallenge.utils.OffsetInterceptor
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
    fun provideOffsetInterceptor(): OffsetInterceptor {
        return OffsetInterceptor()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        networkInterceptor: NetworkInterceptor,
        offsetInterceptor: OffsetInterceptor
    ): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(networkInterceptor)
            .addInterceptor(offsetInterceptor)
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.NYT_API_URL)
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