package com.quan.lam.nytimesmovies.network.api

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApiBuilder {
    /**
     * This base class leaves room for different configuration when needed
     * For example, using some different response format other than JSON
     * Also allowing reusing code for different webservices.
    */
    companion object {
        fun getDefaultBuild(url: String): Retrofit {
            return Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl(url)
                .build()
        }
    }
}