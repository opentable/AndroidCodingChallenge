package com.example.otchallenge.domain.usecase

import com.example.otchallenge.domain.repository.BooksRepository
import javax.inject.Inject

interface GetBooksUseCase {
    operator fun invoke()
}

class GetBooksUseCaseImpl @Inject constructor(
    val booksRepository: BooksRepository,
) : GetBooksUseCase {
    override fun invoke() {

    }
}