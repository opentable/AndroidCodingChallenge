package com.quan.lam.nytimesmovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quan.lam.nytimesmovies.model.MPAARating

/**
 * Default Activity
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
    }

    var movieRatingLimit: MPAARating = MPAARating("NC-17")

    fun setMovieRatingLimit(limit: String) {
        movieRatingLimit = MPAARating(limit)
    }
}
