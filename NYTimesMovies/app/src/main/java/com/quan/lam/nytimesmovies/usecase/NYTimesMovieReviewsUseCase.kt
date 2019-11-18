package com.quan.lam.nytimesmovies.usecase

import com.quan.lam.nytimesmovies.network.api.NYTimesApi
import com.quan.lam.nytimesmovies.network.response.MoviesReviewsResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Concrete implementation of the NYTimesMovieReview API UseCase.
 * Specify the API to use and the interaction that follow.
 */
class NYTimesMovieReviewsUseCase : BaseMovieReviewsUseCase() {

    private val nytimesMovieReivewApi by lazy {
        NYTimesApi.provide()
    }

    override fun execute(offset: Int) {
        cleanUp()
        nytimesMovieReivewApi.fetchMoviesDVDPicksReviews(offset = offset)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> success(result) },
                { error -> error(error) }
            ).track()
    }

    override fun success(response: MoviesReviewsResponse) {
        liveData.value = Result.OnSuccess(response.results)
    }

    override fun error(throwable: Throwable) {
        liveData.value = Result.OnError(throwable)
    }

    companion object {
        fun provide(): BaseMovieReviewsUseCase {
            return NYTimesMovieReviewsUseCase()
        }
    }
}