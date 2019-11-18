package com.quan.lam.nytimesmovies.model

import androidx.lifecycle.MutableLiveData

/**
 * Rating object
 * @param String value of the rating
 */
class MPAARating(var mpaa_rating: String) {
    var order = 0
    init {
        when (mpaa_rating) {
            "G" -> order = 0
            "PG"-> order = 1
            "PG-13" -> order = 2
            "R" -> order = 3
            "NC-17" -> order = 4
            "Not Rated" -> order = 0
            else -> {
                mpaa_rating = "N/A"
                order = 0
            }
        }
    }

    /**
     * Global singleton to hold app age limit setting
     */
    companion object {
        var globalAgeLimit: MutableLiveData<MPAARating> = MutableLiveData()

        init {
            globalAgeLimit.value = MPAARating("NC-17")
        }
    }
}