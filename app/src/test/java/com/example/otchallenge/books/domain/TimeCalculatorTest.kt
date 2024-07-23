package com.example.otchallenge.books.domain

import org.junit.Assert.*
import org.junit.Test
import java.util.Locale

class TimeCalculatorTest {

    private val timeCalculator = TimeCalculator(Locale.getDefault())

    @Test
    fun `given a well formated date, then fromServer return correct local date`() {
        val date = "2024-06-01T22:24:46-00:00"
        assertEquals("Jun-01-2024 04:24 PM", timeCalculator.fromServer(date))
    }

    @Test
    fun `given a bad formated date, then fromServer return empty`() {
        val date = "2024-06-0122:24:46-00:00"
        assertEquals("", timeCalculator.fromServer(date))
    }
}