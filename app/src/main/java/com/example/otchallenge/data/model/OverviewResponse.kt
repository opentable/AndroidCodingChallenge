package com.example.otchallenge.data.model

import com.google.gson.annotations.SerializedName

data class OverviewResponse(
    val status: String,
    val copyright: String,
    @SerializedName("num_results") val numResults: Int,
    val results: Results
)
