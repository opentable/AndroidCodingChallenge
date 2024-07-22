package com.example.otchallenge.books.data

import app.cash.sqldelight.rx2.asObservable
import app.cash.sqldelight.rx2.mapToList
import com.example.otchallenge.Database
import com.example.otchallenge.books.domain.Book
import com.example.otchallenge.books.domain.BooksData
import com.example.otchallenge.books.domain.BooksRepository
import com.example.otchallenge.database.BooksTable
import com.example.otchallenge.di.NYTimesApiKey
import com.example.otchallenge.network.NetworkError
import com.example.otchallenge.network.NoResponseBodyError
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import timber.log.Timber
import javax.inject.Inject

class NYTimesBooksRepository @Inject constructor(
    private val service: Service,
    @NYTimesApiKey private val apiKey: String,
    private val database: Database,
): BooksRepository {

    override fun fetchBooks(offset: Int): Single<Boolean> {
        return service.fetchBooks(apiKey, offset).map { response ->
            if(!response.isSuccessful) {
                Timber.w("fetchAllBooks request was not successful")
                throw NetworkError()
            }
            response.body()
                ?.takeIf { it.status == "OK" }
                ?.let {
                    insertBooks(it.results.books, it.lastModified)
                    it.results.books.isEmpty()
                } ?: throw NoResponseBodyError()
        }
    }

    override fun fetchAllBooks(): Observable<BooksData> {
        return database.booksQueries.selectAll().asObservable().mapToList().map {
            BooksData(
                lastModified = if (it.isNotEmpty()) it.first().last_modified else "",
                books = it.map { book -> book.toBook() }
            )
        }
    }

    override fun insertBooks(books: List<NYTimeBook>, lastModified: String) {
        database.booksQueries.apply {
            transaction {
                books.forEach {
                    it.apply {
                        insert(
                            primary_isbn10 = primaryIsbn10,
                            publisher = publisher,
                            description = description,
                            title = title,
                            author = author,
                            book_image = bookImage,
                            book_image_width = bookImageWidth.toLong(),
                            book_image_height = bookImageHeight.toLong(),
                            isbns = isbns.joinToString(", ") {
                                    isbn -> isbn.convertStringSeparatedByCommas()
                            },
                            last_modified = lastModified
                        )
                    }
                }
            }
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

fun BooksTable.toBook(): Book {
    return Book(
        publisher = publisher,
        description = description,
        title = title,
        author = author,
        image = book_image,
        imageWidth = book_image_width.toInt(),
        imageHeight = book_image_height.toInt(),
        isbns = isbns
    )
}