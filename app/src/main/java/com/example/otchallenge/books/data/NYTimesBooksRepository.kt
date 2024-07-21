package com.example.otchallenge.books.data

import com.example.otchallenge.books.domain.BooksData
import com.example.otchallenge.books.domain.BooksRepository
import com.example.otchallenge.network.NetworkError
import com.example.otchallenge.network.NoResponseBodyError
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber

class NYTimesBooksRepository(
    private val service: Service,
    private val apiKey: String
): BooksRepository {

    override fun fetchBooks(offset: Int): Single<BooksData> {
        return service.fetchBooks(apiKey, offset).map { response ->
            if(!response.isSuccessful) {
                Timber.w("fetchAllBooks request was not successful")
                throw NetworkError()
            }
            response.body()
                ?.takeIf { it.status == "OK" }
                ?.let {
                    BooksData(
                        lastModified = it.lastModified,
                        books = it.results.books.map { book -> book.toDomainBook() }
                    )
                } ?: throw NoResponseBodyError()
        }
    }

    interface Service {
        @GET("svc/books/v3/lists/current/hardcover-fiction.json")
        fun fetchBooks(
            @Query("api-key") apiKey: String,
            @Query("offset") offset: Int
        ): Single<Response<NYTimesBooksResponse>>
    }
}