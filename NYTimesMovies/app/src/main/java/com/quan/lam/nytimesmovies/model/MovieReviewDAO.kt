package com.quan.lam.nytimesmovies.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieReviewDAO(val display_title: String = "",
                          val mpaa_rating: String = "N/A",
                          val critics_pick: Int = 0,
                          val byline: String = "",
                          val headline: String = "",
                          val summary_short: String = "",
                          val publication_date: String = "",
                          val opening_date: String = "",
                          val date_updated: String = "",
                          val link: LinkDAO = LinkDAO(),
                          val multimedia: MultimediaDAO = MultimediaDAO()) : Parcelable