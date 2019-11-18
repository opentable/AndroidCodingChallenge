package com.quan.lam.nytimesmovies.ui.main

import com.quan.lam.nytimesmovies.model.MovieReviewDAO

/**
 * This is a list item counterpart of the MovieReviewDAO
 * Separating UI representation and DAO allow for uncoupling the UI from the data source
 * For example MovieReviewListItem can be construct from a different type of MovieReviewDAO when
 * the need arise.
 */
data class MovieReviewListItem(val display_title: String = "",
                               val mpaa_rating: String = "",
                               val byline: String = "",
                               val headline: String = "",
                               val summary_short: String = "",
                               val publication_date: String = "",
                               val multimedia: String = "") {
    constructor(reviewDAO: MovieReviewDAO): this(reviewDAO.display_title,
        if (reviewDAO.mpaa_rating.isEmpty()) {"N/A"} else {reviewDAO.mpaa_rating},
        reviewDAO.byline,
        if (reviewDAO.headline.contains("Review:")) {
            reviewDAO.headline.substringAfter("Review:").trim()
        } else {reviewDAO.headline},
        reviewDAO.summary_short,
        reviewDAO.publication_date,
        reviewDAO.multimedia.src)
}