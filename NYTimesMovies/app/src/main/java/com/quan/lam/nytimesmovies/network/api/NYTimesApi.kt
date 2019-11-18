package com.quan.lam.nytimesmovies.network.api

import com.quan.lam.nytimesmovies.network.response.MoviesReviewsResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface NYTimesApi {

    /**
     * Fetch Movies Reviews from NYTimes API
     */
    @GET("/svc/movies/v2/reviews/dvd-picks.json")
    fun fetchMoviesDVDPicksReviews(@Query(ORDER) order: String = BY_DATE,
                                   @Query(API_KEY) apiKey: String = DEFAULT_KEY,
                                   @Query(OFFSET) offset:Int = 0): Observable<MoviesReviewsResponse>

    companion object {
        const val BASE_URL = "http://api.nytimes.com/"
        const val DEFAULT_KEY = "KoRB4K5LRHygfjCL2AH6iQ7NeUqDAGAB"
        const val ORDER = "order"
        const val BY_DATE = "by-date"
        const val API_KEY = "api-key"
        const val OFFSET = "offset"

        fun provide(): NYTimesApi {
            val retrofit = RetrofitApiBuilder.getDefaultBuild(BASE_URL)
            return retrofit.create(NYTimesApi::class.java)
        }
    }
}