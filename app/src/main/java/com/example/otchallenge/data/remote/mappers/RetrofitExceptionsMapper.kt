package com.example.otchallenge.data.remote.mappers

import com.example.otchallenge.data.remote.RemoteRequestError
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

val Throwable?.wrappedError: RemoteRequestError
    get() {
        return when (this) {
            is SocketTimeoutException ->
                RemoteRequestError.ConnectionTimeout(cause = this)
            is IOException ->
                RemoteRequestError.NoConnection(cause = this)
            is HttpException ->
                RemoteRequestError.Server(code = code(), cause = this)
            else ->
                RemoteRequestError.Unknown(cause = this)
        }
    }