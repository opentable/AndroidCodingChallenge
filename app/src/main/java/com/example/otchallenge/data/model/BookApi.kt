package com.example.otchallenge.data.model

import com.google.gson.annotations.SerializedName

data class BookApi(
    val rank: Int,
    @SerializedName("rank_last_week") val rankLastWeek: Int,
    @SerializedName("weeks_on_list") val weeksOnList: Int,
    val asterisk: Int,
    val dagger: Int,
    @SerializedName("primary_isbn10") val primaryIsbn10: String,
    @SerializedName("primary_isbn13") val primaryIsbn13: String,
    val publisher: String,
    val description: String,
    val price: String,
    val title: String,
    val author: String,
    val contributor: String,
    @SerializedName("contributor_note") val contributorNote: String,
    @SerializedName("book_image") val bookImage: String,
    @SerializedName("book_image_width") val bookImageWidth: Int,
    @SerializedName("book_image_height") val bookImageHeight: Int,
    @SerializedName("amazon_product_url") val amazonProductUrl: String,
    @SerializedName("age_group") val ageGroup: String,
    @SerializedName("book_review_link") val bookReviewLink: String,
    @SerializedName("first_chapter_link") val firstChapterLink: String,
    @SerializedName("sunday_review_link") val sundayReviewLink: String,
    @SerializedName("article_chapter_link") val articleChapterLink: String,
    val bookUri: String? = ""
)