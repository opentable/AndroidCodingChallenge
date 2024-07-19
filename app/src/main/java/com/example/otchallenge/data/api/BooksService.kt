package com.example.otchallenge.data.api

import com.example.otchallenge.data.model.OverviewResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

//https://api.nytimes.com/svc/books/v3/lists/current/hardcover-fiction.json?api-key=KoRB4K5LRHygfjCL2AH6iQ7NeUqDAGAB&offset=0
interface BooksService {
    @GET("lists/current/hardcover-fiction.json")
    fun getBooks(@Query("api-key") apiKey: String): Single<OverviewResponse>

}