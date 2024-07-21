package com.example.otchallenge.data.model

import com.google.gson.annotations.SerializedName

data class Results(
    @SerializedName("list_name") val listName: String,
    @SerializedName("list_name_encoded") val listNameEncoded: String,
    @SerializedName("bestsellers_date") val bestsellersDate: String,
    @SerializedName("published_date") val publishedDate: String,
    @SerializedName("published_date_description") val publishedDateDescription: String,
    @SerializedName("next_published_date") val nextPublishedDate: String,
    @SerializedName("previous_published_date") val previousPublishedDate: String,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("normal_list_ends_at") val normalListEndsAt: Int,
    val updated: String,
    val books: List<BookApi>,
    val corrections: List<Correction>?
)