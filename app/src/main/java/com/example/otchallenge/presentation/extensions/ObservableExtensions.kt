package com.example.otchallenge.presentation.extensions

import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

fun <T> Observable<T>.subscribe(
    disposables: CompositeDisposable,
    onNext: (T) -> Unit
) {
    subscribe(onNext)
        .also { disposable ->
            disposables.add(disposable)
        }
}