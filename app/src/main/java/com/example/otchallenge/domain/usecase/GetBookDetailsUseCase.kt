package com.example.otchallenge.domain.usecase

import com.example.otchallenge.domain.executor.PostExecutionThread
import com.example.otchallenge.domain.executor.ThreadExecutor
import com.example.otchallenge.domain.mapper.BookDetailMapper
import com.example.otchallenge.domain.repository.BookDetailsRepository
import com.example.otchallenge.presentation.model.BookDetailPresentation
import io.reactivex.Single
import javax.inject.Inject

class GetBookDetailsUseCase @Inject constructor(
    private val bookRepository: BookDetailsRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) : SingleUseCase<Int, BookDetailPresentation>(threadExecutor, postExecutionThread),
    GetBookDetailsUseCaseContract {

    private val mapper = BookDetailMapper()

    override fun buildUseCase(params: Int?): Single<BookDetailPresentation> {
        if (params == null) {
            return Single.error(IllegalArgumentException("Book ID can't be null"))
        }
        return bookRepository.loadBookDetails(params)
            .map {
                mapper.transform(it)
            }
    }

    override fun getBookDetails(id: Int): Single<BookDetailPresentation> {
        return execute(id)
    }
}