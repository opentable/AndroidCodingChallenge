package com.example.otchallenge.data.network.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BooksResponse(

    @Json(name = "copyright")
    val copyright: String? = null,

    @Json(name = "last_modified")
    val lastModified: String? = null,

    @Json(name = "results")
    val results: Results? = null,

    @Json(name = "num_results")
    val numResults: Int? = null,

    @Json(name = "status")
    val status: String? = null,
)

@JsonClass(generateAdapter = true)
data class BuyLinksItem(

    @Json(name = "name")
    val name: String? = null,

    @Json(name = "url")
    val url: String? = null,
)

@JsonClass(generateAdapter = true)
data class IsbnItem(

    @Json(name = "isbn13")
    val isbn13: String? = null,

    @Json(name = "isbn10")
    val isbn10: String? = null,
)

@JsonClass(generateAdapter = true)
data class BooksItem(

    @Json(name = "isbns")
    val isbns: List<IsbnItem?>? = null,

    @Json(name = "contributor_note")
    val contributorNote: String? = null,

    @Json(name = "asterisk")
    val asterisk: Int? = null,

    @Json(name = "description")
    val description: String? = null,

    @Json(name = "primary_isbn10")
    val primaryIsbn10: String? = null,

    @Json(name = "primary_isbn13")
    val primaryIsbn13: String? = null,

    @Json(name = "title")
    val title: String? = null,

    @Json(name = "weeks_on_list")
    val weeksOnList: Int? = null,

    @Json(name = "article_chapter_link")
    val articleChapterLink: String? = null,

    @Json(name = "book_image_width")
    val bookImageWidth: Int? = null,

    @Json(name = "contributor")
    val contributor: String? = null,

    @Json(name = "amazon_product_url")
    val amazonProductUrl: String? = null,

    @Json(name = "price")
    val price: String? = null,

    @Json(name = "book_uri")
    val bookUri: String? = null,

    @Json(name = "rank")
    val rank: Int? = null,

    @Json(name = "dagger")
    val dagger: Int? = null,

    @Json(name = "author")
    val author: String? = null,

    @Json(name = "age_group")
    val ageGroup: String? = null,

    @Json(name = "buy_links")
    val buyLinks: List<BuyLinksItem?>? = null,

    @Json(name = "sunday_review_link")
    val sundayReviewLink: String? = null,

    @Json(name = "book_review_link")
    val bookReviewLink: String? = null,

    @Json(name = "book_image_height")
    val bookImageHeight: Int? = null,

    @Json(name = "book_image")
    val bookImage: String? = null,

    @Json(name = "publisher")
    val publisher: String? = null,

    @Json(name = "rank_last_week")
    val rankLastWeek: Int? = null,

    @Json(name = "first_chapter_link")
    val firstChapterLink: String? = null,
)

@JsonClass(generateAdapter = true)
data class Results(

    @Json(name = "next_published_date")
    val nextPublishedDate: String? = null,

    @Json(name = "bestsellers_date")
    val bestsellersDate: String? = null,

    @Json(name = "books")
    val books: List<BooksItem?>? = null,

    @Json(name = "published_date_description")
    val publishedDateDescription: String? = null,

    @Json(name = "normal_list_ends_at")
    val normalListEndsAt: Int? = null,

    @Json(name = "list_name")
    val listName: String? = null,

    @Json(name = "list_name_encoded")
    val listNameEncoded: String? = null,

    @Json(name = "previous_published_date")
    val previousPublishedDate: String? = null,

    @Json(name = "display_name")
    val displayName: String? = null,

    @Json(name = "published_date")
    val publishedDate: String? = null,

    @Json(name = "updated")
    val updated: String? = null,
)
