package com.example.otchallenge.data.repository

import com.example.otchallenge.data.api.BooksService
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.data.mapper.BookApiMapper
import com.example.otchallenge.domain.mapper.BookSummaryEntityMapper
import com.example.otchallenge.domain.model.BookSummary
import com.example.otchallenge.domain.repository.BookListRepository
import io.reactivex.Single
import retrofit2.HttpException
import javax.inject.Inject

class BookListRepositoryImpl @Inject constructor(
    private val bookService: BooksService,
    private val bookDao: BookDao
) : BookListRepository {

    private val apiMapper = BookApiMapper()
    private val mapper = BookSummaryEntityMapper()

    override fun getBooks(): Single<List<BookSummary>> {
        return bookService.getBooks()
            .flatMap { response ->
                if (response.isSuccessful) {
                    val books = response.body()?.results?.books?.map { bookApi ->
                        apiMapper.transform(bookApi)
                    } ?: emptyList()

                    Single.fromCallable {
                        bookDao.insertBooks(books)
                    }.flatMap {
                        bookDao.getBooks().map { booksWithIds ->
                            booksWithIds.map {
                                mapper.reverseTransform(it)
                            }
                        }
                    }
                } else {
                    Single.error(HttpException(response))
                }
            }.onErrorResumeNext { throwable ->
                bookDao.getBooks()
                    .map { summaries ->
                        summaries.map {
                            mapper.reverseTransform(it)
                        }
                    }
                    .onErrorResumeNext {
                        Single.error(throwable)
                    }
            }
    }
}