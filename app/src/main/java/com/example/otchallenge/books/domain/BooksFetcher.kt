package com.example.otchallenge.books.domain

import com.care.sdk.utils.StringResources
import com.example.otchallenge.R
import com.example.otchallenge.network.NetworkError
import com.example.otchallenge.network.NoResponseBodyError
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class BooksFetcher @Inject constructor(
    private val booksRepository: BooksRepository,
    private val timeCalculator: TimeCalculator,
    private val stringResources: StringResources
) {
    private var biggestOffset = -1

    /**
     * Since this can be a fast calling method due to the nature of scrolling, it's better to
     * lock it to avoid repeated calls.
     */
    @Synchronized
    fun fetchBooksFromRemote(offset: Int): Single<Boolean> {
        if(!shouldFetchBooks(offset))
            return Single.just(false)

        return booksRepository.fetchBooksFromRemote(offset)
            .doOnSuccess { onRemoteSuccess(offset) }
            .onErrorResumeNext(::onRemoteError)
    }

    fun retryLastFetch(): Single<Boolean> {
        if (biggestOffset == -1) biggestOffset = 0
        return booksRepository.fetchBooksFromRemote(biggestOffset)
            .doOnSuccess { onRemoteSuccess(biggestOffset) }
            .onErrorResumeNext(::onRemoteError)
    }

    private fun onRemoteSuccess(offset: Int) {
        saveBiggestOffset(offset)
    }

    private fun onRemoteError(error: Throwable): Single<Boolean> {
        return when(error) {
            is NoResponseBodyError -> {
                Single.error(NoResponseBodyError(stringResources.getString(R.string.no_response_body_error)))
            }
            else ->  {
                Single.error(NetworkError(stringResources.getString(R.string.network_error)))
            }
        }
    }

    fun fetchBooksFromLocal(): Observable<BooksData> {
        return booksRepository.fetchAllBooks()
            .map { onLocalSuccess(it) }
            .onErrorResumeNext(::onLocalError)
    }

    private fun onLocalSuccess(data: BooksData): BooksData {
        return BooksData(
            lastModified = timeCalculator.fromServer(data.lastModified),
            books = data.books,
        )
    }

    private fun onLocalError(error: Throwable): Observable<BooksData> {
        return when(error) {
            is NoResponseBodyError -> {
                Observable.error(NoResponseBodyError(stringResources.getString(R.string.no_response_body_error)))
            }
            else -> {
                Observable.error(NetworkError(stringResources.getString(R.string.network_error)))
            }
        }
    }

    private fun shouldFetchBooks(offset: Int): Boolean {
        return offset > biggestOffset
    }

    /**
     * Keep track of the biggestOffset that fetched data successfully to avoid unnecessary calls
     */
    private fun saveBiggestOffset(offset: Int) {
        biggestOffset = if (offset > biggestOffset) offset else return
    }
}