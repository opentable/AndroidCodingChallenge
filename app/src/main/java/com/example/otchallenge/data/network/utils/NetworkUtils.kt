package com.example.otchallenge.data.network.utils

import com.example.otchallenge.domain.ResultData
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException


fun makeOkHttpClient(vararg interceptors: Interceptor?): OkHttpClient {
    return OkHttpClient.Builder().apply {
        retryOnConnectionFailure(false)
        interceptors().addAll(interceptors.filterNotNull())
    }.build()
}

fun createRetrofit(
    baseUrl: String,
    isDebug: Boolean,
): Retrofit {
    val okHttpClient = makeOkHttpClient(
        CommonInterceptors.okHttpLoggingInterceptor(isDebug)
    )
    return Retrofit.Builder().apply {
        baseUrl(baseUrl)
        addConverterFactory(MoshiConverterFactory.create())
        client(okHttpClient)
    }.build()
}

fun <T> getResultFrom(response: Response<T>): ResultData<T> {
    return try {
        getResultFromResponse(response)
    } catch (e: Throwable) {
        e.printStackTrace()
        val message = when (e) {
            is SocketTimeoutException -> "TIMEOUT_ERROR"
            is HttpException -> "HTTP_ERROR"
            is IOException -> "IO_ERROR"
            else -> e.message ?: "UNKNOWN_ERROR"
        }
        ResultData.Error(Throwable(message))
    }
}

private fun <R> getResultFromResponse(response: Response<R>): ResultData<R> {
    return if (response.isSuccessful) {
        response.body()?.let { body ->
            ResultData.Success(body)
        } ?: ResultData.Error(Throwable("CODE_EMPTY_BODY"))
    } else {
        ResultData.Error(Throwable("NOT_SUCCESS_CALL"))
    }
}
