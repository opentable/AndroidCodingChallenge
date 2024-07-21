package com.example.otchallenge.domain.usecase

import com.example.otchallenge.domain.repository.BookListRepositoryContract
import com.example.otchallenge.presentation.model.BookPresentation
import com.example.otchallenge.utils.BookMapper
import io.reactivex.Single
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(private val bookRepository: BookListRepositoryContract) :
    GetBooksUseCaseContract {
    override fun execute(): Single<List<BookPresentation>> {
        return bookRepository.loadBooks().map { bookSummary ->
            bookSummary.map { BookMapper.mapDomainSummaryToPresentation(it) }
        }
    }
}