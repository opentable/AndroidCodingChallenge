package com.example.otchallenge

import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

// Factory class for creating a ServerException (ie Retrofit HttpException)
interface RemoteServerExceptionFactory {
    operator fun invoke(code: Int): Throwable
}

class RetrofitHttpExceptionFactory @Inject constructor() : RemoteServerExceptionFactory {
    override fun invoke(code: Int): Throwable {
        return HttpException(
            Response.error<Any>(code, "".toResponseBody())
        )
    }
}