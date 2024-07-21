package com.example.otchallenge.data.api

import com.example.otchallenge.BuildConfig
import com.example.otchallenge.data.model.OverviewResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksService {
    @GET("lists/current/hardcover-fiction.json")
    fun getBooks(@Query("api-key") apiKey: String = BuildConfig.NYT_API_KEY):
            Single<Response<OverviewResponse>>

}