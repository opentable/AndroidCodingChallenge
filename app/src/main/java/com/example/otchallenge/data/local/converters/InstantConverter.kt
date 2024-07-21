package com.example.otchallenge.data.local.converters

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {

    @TypeConverter
    fun fromLong(instant: Long): Instant {
        return Instant.ofEpochSecond(instant)
    }

    @TypeConverter
    fun toLong(instant: Instant): Long {
        return instant.toEpochMilli() / 1000
    }

}