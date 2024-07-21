package com.example.otchallenge.domain.usecase

import com.example.otchallenge.presentation.model.BookDetailPresentation
import io.reactivex.Single

interface GetBookDetailsUseCaseContract {
    fun getBookDetails(id: Int): Single<BookDetailPresentation>
}