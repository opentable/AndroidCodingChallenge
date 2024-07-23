package com.example.otchallenge.books.domain

import com.care.sdk.utils.StringResources
import com.example.otchallenge.R

class FakeStringResources: StringResources {

    val noResponseBodyError = "noResponseBodyError"
    val networkError = "network_error"
    val lastModificationDate = "last modification date"

    override fun getString(string: Int, vararg args: String): String {
        return when(string) {
            R.string.no_response_body_error -> noResponseBodyError
            R.string.network_error -> networkError
            R.string.last_modification -> lastModificationDate
            else -> "String not implemented"
        }
    }
}