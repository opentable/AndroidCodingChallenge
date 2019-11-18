package com.quan.lam.nytimesmovies.usecase
import androidx.lifecycle.LiveData

/**
 * The interface of a use case
 * A UseCase is an implementation of the command design patter, allowing the encapsulation
 * of a single user interaction into an executable command.
 */
interface UseCase<T> {

    fun getLiveData(): LiveData<T>

    fun cleanUp()
}
