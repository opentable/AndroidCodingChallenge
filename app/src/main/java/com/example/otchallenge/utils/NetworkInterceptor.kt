package com.example.otchallenge.utils

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class NetworkInterceptor @Inject constructor(private val connectivityChecker: ConnectivityChecker) :
    Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!connectivityChecker.isNetworkConnected()) {
            throw NoConnectivityException()
        }

        val request = chain.request()
        return try {
            chain.proceed(request)
        } catch (e: SocketTimeoutException) {
            throw TimeoutException()
        } catch (e: IOException) {
            throw NetworkException()
        }
    }
}

class NoConnectivityException : IOException("No connectivity exception")
class TimeoutException : IOException("Timeout exception")
class NetworkException : IOException("Network exception")