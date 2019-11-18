package com.quan.lam.nytimesmovies.usecase

import com.quan.lam.nytimesmovies.model.MovieReviewDAO
import com.quan.lam.nytimesmovies.network.response.MoviesReviewsResponse

/**
 * Base implementation of a Movie Review Fetching Use Case
 * Subsequents implementation can define the data source and implementation detail
 */
abstract class BaseMovieReviewsUseCase : BaseUseCase<BaseMovieReviewsUseCase.Result>() {
    sealed class Result {
        data class OnSuccess(val result: List<MovieReviewDAO>) : Result()
        data class OnError(val throwable: Throwable): Result()
    }

    protected abstract fun success(response: MoviesReviewsResponse)

    protected abstract fun error(throwable: Throwable)

    abstract fun execute(offset: Int = 0)
}