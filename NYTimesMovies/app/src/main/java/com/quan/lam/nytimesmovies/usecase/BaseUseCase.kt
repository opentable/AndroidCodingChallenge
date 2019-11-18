package com.quan.lam.nytimesmovies.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Base Implementation of a UseCase
 */
abstract class BaseUseCase<T>(
        private val compositeDisposable: CompositeDisposable = CompositeDisposable(),
        protected val liveData: MutableLiveData<T> = MutableLiveData()
) : UseCase<T> {

    protected fun Disposable.track() {
        compositeDisposable.add(this)
    }

    override fun getLiveData(): LiveData<T> = liveData

    override fun cleanUp() {
        compositeDisposable.clear()
    }
}
