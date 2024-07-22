package com.example.otchallenge.domain.usecase

import com.example.otchallenge.domain.repository.BookListRepository
import com.example.otchallenge.domain.executor.PostExecutionThread
import com.example.otchallenge.domain.executor.ThreadExecutor
import com.example.otchallenge.domain.mapper.BookSummaryMapper
import com.example.otchallenge.presentation.model.BookPresentation
import io.reactivex.Single
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(
    private val bookRepository: BookListRepository,
    threadExecutor: ThreadExecutor,
    postExecutionThread: PostExecutionThread
) :
    SingleUseCase<Any?, List<BookPresentation>>(threadExecutor, postExecutionThread),
    GetBooksUseCaseContract {

    override fun buildUseCase(params: Any?): Single<List<BookPresentation>> {
        return bookRepository.getBooks()
            .map { books ->
                BookSummaryMapper().transformCollection(books)
            }
    }

    override fun getBooks(): Single<List<BookPresentation>> {
        return execute()
    }
}