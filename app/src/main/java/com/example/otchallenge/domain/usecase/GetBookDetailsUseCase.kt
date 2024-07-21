package com.example.otchallenge.domain.usecase

import android.util.Log
import com.example.otchallenge.domain.repository.BookDetailsRepositoryContract
import com.example.otchallenge.presentation.model.BookDetailPresentation
import com.example.otchallenge.utils.BookMapper
import io.reactivex.Single
import javax.inject.Inject

class GetBookDetailsUseCase @Inject constructor(
    private val bookRepository: BookDetailsRepositoryContract
) : GetBookDetailsUseCaseContract {

    override fun getBookDetails(id: Int): Single<BookDetailPresentation> {
        return bookRepository.loadBookDetails(id)
            .doOnSubscribe { Log.d("GetBookDetailsUseCase", "getBookDetails: Subscribed") }
            .doOnSuccess { Log.d("GetBookDetailsUseCase", "getBookDetails: Success - $it") }
            .doOnError { Log.e("GetBookDetailsUseCase", "getBookDetails: Error", it) }
            .map { book ->
                Log.d("GetBookDetailsUseCase", "getBookDetails: Book: $book")
                val bookPresentation = BookMapper.mapDomainToDetailPresentation(book)
                Log.d(
                    "GetBookDetailsUseCase",
                    "getBookDetails: BookPresentation: $bookPresentation"
                )
                bookPresentation
            }
    }
}