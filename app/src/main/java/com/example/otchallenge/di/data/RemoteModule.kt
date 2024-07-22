package com.example.otchallenge.di.data

import com.example.otchallenge.data.remote.BooksApi
import com.example.otchallenge.data.remote.NYTApiConstants
import com.example.otchallenge.data.remote.converters.MoshiLocalDateConverter
import com.example.otchallenge.data.remote.converters.MoshiZonedDateTimeConverter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class RemoteModule {

    @Provides
    @Singleton
    fun provideOkHttp3Client(): OkHttpClient {
        return OkHttpClient.Builder()
            .addHttpLoggingInterceptor()
            .addApiKeyInterceptor()
            .build()
    }

    private fun OkHttpClient.Builder.addHttpLoggingInterceptor(): OkHttpClient.Builder {
        return addInterceptor (
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
    }

    private fun OkHttpClient.Builder.addApiKeyInterceptor(): OkHttpClient.Builder {
        return addInterceptor { chain ->
            chain.request().url.newBuilder().run {
                addQueryParameter(
                    name = NYTApiConstants.ApiKey.first,
                    value = NYTApiConstants.ApiKey.second
                )
            }.build().let { url ->
                chain.proceed(chain.request().newBuilder().url(url).build())
            }
        }
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(MoshiLocalDateConverter())
            .add(MoshiZonedDateTimeConverter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        client: OkHttpClient,
        moshi: Moshi
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(NYTApiConstants.BaseUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    fun provideBooksApi(
        retrofit: Retrofit
    ): BooksApi {
        return retrofit.create(BooksApi::class.java)
    }
}