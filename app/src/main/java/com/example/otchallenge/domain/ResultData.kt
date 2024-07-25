package com.example.otchallenge.domain

sealed class ResultData<out T> {
    data class Success<out T>(val data: T) : ResultData<T>()
    data class Error(val error: Throwable) : ResultData<Nothing>()
}