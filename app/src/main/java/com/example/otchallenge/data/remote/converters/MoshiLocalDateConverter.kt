package com.example.otchallenge.data.remote.converters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate

class MoshiLocalDateConverter {

    @ToJson
    fun toString(date: LocalDate): String {
        return date.toString()
    }

    @FromJson
    fun fromString(date: String): LocalDate {
        return LocalDate.parse(date)
    }

}