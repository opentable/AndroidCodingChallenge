package com.example.otchallenge.domain.usecase

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class UseCase<Params, R : Any> {
    private var disposable: CompositeDisposable = CompositeDisposable()

    protected abstract fun buildUseCase(params: Params? = null): R

    /**
     * Dispose from current subscription
     */
    fun dispose() {
        if (!disposable.isDisposed) {
            disposable.dispose()
        }
    }

    /**
     * Sets the current subscription
     *
     * @param subscription
     */
    protected fun addDisposable(subscription: Disposable) {
        disposable.add(subscription)
    }
}