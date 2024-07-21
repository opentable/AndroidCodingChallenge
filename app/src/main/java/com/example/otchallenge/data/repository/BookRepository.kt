package com.example.otchallenge.data.repository

import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.domain.model.Book
import com.example.otchallenge.domain.model.BookSummary
import com.example.otchallenge.domain.usecase.BookLoader
import com.example.otchallenge.utils.BookMapper
import io.reactivex.Single
import retrofit2.HttpException
import javax.inject.Inject

class BookRepository @Inject constructor(
    private val bookService: BooksService,
    private val bookDao: BookDao
) : BookLoader {

    override fun loadBooks(): Single<List<BookSummary>> {
        return bookService.getBooks()
            .flatMap { response ->
                if (response.isSuccessful) {
                    val books = response.body()?.results?.books?.map { bookApi ->
                        BookMapper.mapApiToEntity(bookApi)
                    } ?: emptyList()

                    bookDao.insertBooks(books)
                    Single.just(books.map { BookMapper.mapEntityToDomainSummary(it) })
                } else {
                    Single.error(HttpException(response))
                }
            }
            .onErrorResumeNext { throwable ->
                bookDao.getBooks().map { summaries ->
                    summaries.map { BookMapper.mapEntitySummaryToDomainSummary(it) }
                }.onErrorResumeNext { Single.error(throwable) }
            }
    }

    override fun loadBookDetails(id: Int): Single<Book> {
        return bookDao.getBookById(id).map { BookMapper.mapEntityToDomain(it) }
    }
}