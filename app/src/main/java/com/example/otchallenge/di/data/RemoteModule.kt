package com.example.otchallenge.di.data

import com.example.otchallenge.data.remote.BooksApi
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
                    name = "api-key",
                    value = "KoRB4K5LRHygfjCL2AH6iQ7NeUqDAGAB"
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
            .baseUrl("https://api.nytimes.com")
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
        /*
        return object : BooksApi {
            override fun getBookList(
                name: String,
                date: String,
                offset: Int
            ): BookListResponse {
                Log.e("offset", "$offset")
                val results = if (offset < 40) 20 else 15
                return BookListResponse(
                        numberOfResults = results,
                        lastModified = ZonedDateTime.now(),
                        results = BookListResultResponse(
                            listName = "hardcover-fiction",
                            listNameEncoded = "hardcover-fiction",
                            previousPublishedDate = LocalDate.now(),
                            normalListEndsAt = 55,
                            books = List(results) { i ->
                                BookItemResponse(
                                    primaryIsbn13 = "${i + offset}",
                                    title = "book${i + offset}",
                                    author = "author",
                                    description = "",
                                    bookImage = "",
                                    rank = i + offset
                                )
                            }
                        )
                    )
            }
        }

         */
    }
}