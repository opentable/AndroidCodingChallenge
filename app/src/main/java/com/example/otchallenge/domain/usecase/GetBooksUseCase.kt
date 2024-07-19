package com.example.otchallenge.domain.usecase

import com.example.otchallenge.data.model.Book
import com.example.otchallenge.data.repository.BookRepository
import io.reactivex.Single
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(private val repository: BookRepository) {
    fun execute(apiKey: String): Single<List<Book>> = repository.getBooks(apiKey)
}