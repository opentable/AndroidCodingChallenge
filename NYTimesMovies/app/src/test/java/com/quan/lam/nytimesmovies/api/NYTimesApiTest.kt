package com.quan.lam.nytimesmovies.api

import com.quan.lam.nytimesmovies.network.api.NYTimesApi
import org.junit.Test

class NYTimesApiTest {
    @Test
    fun provideTest() {
        assert(NYTimesApi.provide() is NYTimesApi)
    }
}