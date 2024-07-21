package com.example.otchallenge.data.remote.converters

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.ZonedDateTime

class MoshiZonedDateTimeConverter {

    @FromJson
    fun fromString(dateTime: String): ZonedDateTime {
        return ZonedDateTime.parse(dateTime)
    }

    @ToJson
    fun toString(dateTime: ZonedDateTime): String {
        return dateTime.toString()
    }

}