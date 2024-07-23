package com.example.otchallenge.books.domain

import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class TimeCalculator @Inject constructor(
    private val locale: Locale
) {

    /**
     * Transforms a date with format 2024-07-17T22:24:46-04:00 to Jul-17-2024 10:24 pm
     */
    fun fromServer(date: String): String {
        try {
            val inputDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", locale)
            val outputDateFormat = SimpleDateFormat("MMM-dd-yyyy hh:mm a", locale)
            val formattedDate = inputDateFormat.parse(date) ?: return ""
            return outputDateFormat.format(formattedDate)
        } catch (e: Exception) {
            return ""
        }
    }
}