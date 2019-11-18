package com.quan.lam.nytimesmovies.network.response

import android.os.Parcelable
import com.quan.lam.nytimesmovies.model.MovieReviewDAO
import kotlinx.android.parcel.Parcelize

/**
 * Model of NYTimes API response
 */
@Parcelize
data class MoviesReviewsResponse(val status:String = "",
                            val copyright:String = "",
                            val has_more:Boolean = false,
                            val num_results:Int = 0,
                            val results: List<MovieReviewDAO> = ArrayList()) : Parcelable
