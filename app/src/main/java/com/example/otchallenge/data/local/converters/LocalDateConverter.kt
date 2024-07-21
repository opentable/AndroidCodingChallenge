package com.example.otchallenge.data.local.converters

import androidx.room.TypeConverter
import java.time.LocalDate

class LocalDateConverter {

    @TypeConverter
    fun fromLong(date: Long): LocalDate {
        return LocalDate.ofEpochDay(date)
    }

    @TypeConverter
    fun toLong(date: LocalDate): Long {
        return date.toEpochDay()
    }

}