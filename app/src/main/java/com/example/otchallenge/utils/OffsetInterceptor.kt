package com.example.otchallenge.utils

import com.example.otchallenge.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class OffsetInterceptor  : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val originalUrl = originalRequest.url()

        // Add Offset to original URL
        val newUrl = originalUrl.newBuilder()
            .addQueryParameter("offset", BuildConfig.OFFSET)
            .build()

        // Create new request
        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}