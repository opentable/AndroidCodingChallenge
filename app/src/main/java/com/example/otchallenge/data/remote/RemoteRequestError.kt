package com.example.otchallenge.data.remote

sealed class RemoteRequestError(cause: Throwable?) : Exception(cause) {
    class ConnectionTimeout(cause: Throwable?): RemoteRequestError(cause)
    class NoConnection(cause: Throwable?): RemoteRequestError(cause)
    class Server(val code: Int, cause: Throwable?): RemoteRequestError(cause)
    class Unknown(cause: Throwable?): RemoteRequestError(cause)
}