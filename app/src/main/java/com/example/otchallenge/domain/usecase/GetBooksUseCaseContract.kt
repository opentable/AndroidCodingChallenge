package com.example.otchallenge.domain.usecase

import com.example.otchallenge.presentation.model.BookPresentation
import io.reactivex.Single

interface GetBooksUseCaseContract {
    fun getBooks(): Single<List<BookPresentation>>
}