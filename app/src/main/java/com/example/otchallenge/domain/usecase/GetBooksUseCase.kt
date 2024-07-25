package com.example.otchallenge.domain.usecase

import com.example.otchallenge.domain.ResultData
import com.example.otchallenge.domain.entity.ItemBook
import com.example.otchallenge.domain.repository.BooksRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

interface GetBooksUseCase {
    suspend operator fun invoke(): ResultData<List<ItemBook>>
}

@ViewModelScoped
class GetBooksUseCaseImpl @Inject constructor(
    private val booksRepository: BooksRepository,
) : GetBooksUseCase {
    override suspend fun invoke(): ResultData<List<ItemBook>> {
        return booksRepository.getBooks()
    }
}