package com.quan.lam.nytimesmovies.api

import com.quan.lam.nytimesmovies.network.api.RetrofitApiBuilder
import org.junit.Test
import retrofit2.Retrofit

class RetrofitApiBuilderTest {
    val BASE_URL = "http://api.nytimes.com/"
    @Test
    fun buildTest() {
        val retrofitApiBuilder = RetrofitApiBuilder

        assert(retrofitApiBuilder.getDefaultBuild(BASE_URL) is Retrofit)
    }
}