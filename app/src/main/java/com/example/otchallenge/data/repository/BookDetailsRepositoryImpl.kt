package com.example.otchallenge.data.repository

import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.domain.mapper.BookEntityMapper
import com.example.otchallenge.domain.model.Book
import com.example.otchallenge.domain.repository.BookDetailsRepository
import io.reactivex.Single
import javax.inject.Inject

class BookDetailsRepositoryImpl @Inject constructor(
    private val bookDao: BookDao
) : BookDetailsRepository {

    private val mapper = BookEntityMapper()

    override fun loadBookDetails(id: Int): Single<Book> {
        return bookDao.getBookById(id)
            .map { entity ->
                val domain = mapper.transform(entity)
                domain
            }
    }
}