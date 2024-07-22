package com.example.otchallenge.domain.repository

import com.example.otchallenge.domain.model.Book
import io.reactivex.Single

interface BookDetailsRepository {
    fun loadBookDetails(id: Int): Single<Book>
}