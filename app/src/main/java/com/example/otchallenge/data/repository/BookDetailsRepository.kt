package com.example.otchallenge.data.repository

import android.util.Log
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.domain.model.Book
import com.example.otchallenge.domain.repository.BookDetailsRepositoryContract
import com.example.otchallenge.utils.BookMapper
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class BookDetailsRepository @Inject constructor(
    private val bookDao: BookDao
) : BookDetailsRepositoryContract {
    override fun loadBookDetails(id: Int): Single<Book> {
        return bookDao.getBookById(id)
            .map { entity ->
                Log.d("BookDetailsRepository", "Mapping entity to domain: $entity")
                BookMapper.mapEntityToDomain(entity)
            }
            .subscribeOn(Schedulers.io()) // Operación de base de datos en hilo de fondo
            .observeOn(AndroidSchedulers.mainThread()) // Resultado en hilo principal
            .doOnSuccess { book ->
                Log.d("BookDetailsRepository", "Book details loaded: ${book.title}")
            }
            .doOnError { error ->
                Log.e("BookDetailsRepository", "Error loading book details", error)
            }
    }
}