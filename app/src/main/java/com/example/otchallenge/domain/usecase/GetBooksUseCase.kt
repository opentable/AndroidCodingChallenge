package com.example.otchallenge.domain.usecase

import com.example.otchallenge.data.repository.BookRepository
import com.example.otchallenge.presentation.model.BookDetailPresentation
import com.example.otchallenge.presentation.model.BookPresentation
import com.example.otchallenge.utils.BookMapper
import io.reactivex.Single
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(private val bookRepository: BookRepository) :
    GetBooksUseCaseContract {
    override fun execute(): Single<List<BookPresentation>> {
        return bookRepository.loadBooks().map { bookSummary ->
            bookSummary.map { BookMapper.mapDomainSummaryToPresentation(it) }
        }
    }

    override fun getBookDetails(id: Int): Single<BookDetailPresentation> {
        return bookRepository.loadBookDetails(id).map {
            BookMapper.mapDomainToDetailPresentation(it)
        }
    }
}