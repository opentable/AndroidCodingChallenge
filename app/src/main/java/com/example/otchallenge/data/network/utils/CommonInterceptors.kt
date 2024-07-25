package com.example.otchallenge.data.network.utils

import com.example.otchallenge.BuildConfig
import okhttp3.logging.HttpLoggingInterceptor


object CommonInterceptors {

    fun okHttpLoggingInterceptor(
        debug: Boolean = BuildConfig.DEBUG,
    ): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (debug) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
}