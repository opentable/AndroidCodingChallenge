package com.example.otchallenge.data.remote

import com.example.otchallenge.data.remote.responses.BookListResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface BooksApi {

    @GET("svc/books/v3/lists/{date}/{name}.json")
    fun getBookList(
        @Path("name") name: String = "hardcover-fiction",
        @Path("date") date: String = "current",
        @Query("offset") offset: Int = 0
    ): Single<BookListResponse>

}