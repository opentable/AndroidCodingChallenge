package com.example.otchallenge.network

class NetworkError(error: String = ""): Throwable(error)

class NoResponseBodyError(error: String = ""): Throwable(error)