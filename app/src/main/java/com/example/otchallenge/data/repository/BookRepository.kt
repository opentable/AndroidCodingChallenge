package com.example.otchallenge.data.repository

import android.util.Log
import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.domain.model.Book
import com.example.otchallenge.domain.model.BookSummary
import com.example.otchallenge.domain.usecase.BookLoader
import com.example.otchallenge.utils.BookMapper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookService: BooksService,
    private val bookDao: BookDao
) : BookLoader {

    override fun loadBooks(): Single<List<BookSummary>> {
        return bookService.getBooks()
            .subscribeOn(Schedulers.io())
            .flatMap { response ->
                if (response.isSuccessful) {
                    val books = response.body()?.results?.books?.map { bookApi ->
                        BookMapper.mapApiToEntity(bookApi)
                    } ?: emptyList()

                    // Insert books and retrieve generated IDs
                    Single.fromCallable {
                        bookDao.insertBooks(books)
                    }.flatMap {
                        // Fetch books with their generated IDs from the database
                        bookDao.getBooks().map { booksWithIds ->
                            booksWithIds.map { BookMapper.mapEntitySummaryToDomainSummary(it) }
                        }
                    }
                } else {
                    Single.error(HttpException(response))
                }
            }
            .onErrorResumeNext { throwable ->
                bookDao.getBooks().map { summaries ->
                    summaries.map { BookMapper.mapEntitySummaryToDomainSummary(it) }
                }.onErrorResumeNext { Single.error(throwable) }
                    .subscribeOn(Schedulers.io())
            }
            .observeOn(AndroidSchedulers.mainThread())
    }

    override fun loadBookDetails(id: Int): Single<Book> {
        return bookDao.getBookById(id)
            .map { entity ->
                Log.d("BookRepository", "Mapping entity to domain: $entity")
                val book = BookMapper.mapEntityToDomain(entity)
                Log.d("BookRepository", "Book domain: $book")
                book
            }
            .subscribeOn(Schedulers.io()) // Operación de base de datos en hilo de fondo
            .observeOn(AndroidSchedulers.mainThread()) // Resultado en hilo principal
            .doOnSuccess { book ->
                Log.d("BookRepository", "Book details loaded: ${book.title}")
            }
            .doOnError { error ->
                Log.e("BookRepository", "Error loading book details", error)
            }
    }
}