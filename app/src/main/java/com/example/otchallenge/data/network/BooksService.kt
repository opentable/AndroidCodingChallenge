package com.example.otchallenge.data.network

import com.example.otchallenge.data.network.models.BooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BooksService {

    @GET("svc/books/v3/lists/current/hardcover-fiction.json")
    suspend fun getBooks(
        @Query("api-key") apikey: String,
        @Query("offset") offset: String = "0",
    ): Response<BooksResponse>
}