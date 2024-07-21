package com.example.otchallenge.data.repository

import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.domain.model.BookSummary
import com.example.otchallenge.domain.repository.BookListRepositoryContract
import com.example.otchallenge.utils.BookMapper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.HttpException
import javax.inject.Inject

class BookListRepository @Inject constructor(
    private val bookService: BooksService,
    private val bookDao: BookDao
) : BookListRepositoryContract {

    override fun loadBooks(): Single<List<BookSummary>> {
        return bookService.getBooks()
            .subscribeOn(Schedulers.io()) // Operación de red en hilo de fondo
            .flatMap { response ->
                if (response.isSuccessful) {
                    val books = response.body()?.results?.books?.map { bookApi ->
                        BookMapper.mapApiToEntity(bookApi)
                    } ?: emptyList()

                    Single.fromCallable {
                        bookDao.insertBooks(books)
                    }
                        .flatMap {
                            bookDao.getBooks().map { booksWithIds ->
                                booksWithIds.map { BookMapper.mapEntitySummaryToDomainSummary(it) }
                            }
                        }
                        .subscribeOn(Schedulers.io()) // Operación de base de datos en hilo de fondo
                } else {
                    Single.error(HttpException(response))
                }
            }
            .onErrorResumeNext { throwable ->
                bookDao.getBooks()
                    .map { summaries ->
                        summaries.map { BookMapper.mapEntitySummaryToDomainSummary(it) }
                    }
                    .onErrorResumeNext { Single.error(throwable) }
                    .subscribeOn(Schedulers.io()) // Operación de base de datos en hilo de fondo
            }
            .observeOn(AndroidSchedulers.mainThread()) // Resultado en hilo principal
    }
}