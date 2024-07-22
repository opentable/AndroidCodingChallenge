package com.example.otchallenge.presentation.extensions

import com.example.otchallenge.presentation.components.AlertDialogFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

inline fun <reified T: AlertDialogFragment.Event> Observable<AlertDialogFragment.Event>.subscribeToEvent(
    subscriptions: CompositeDisposable,
    crossinline onNext: (T) -> Unit
) {
    subscribe(subscriptions) { event ->
        if (event is T) {
            onNext(event)
        }
    }
}

inline fun <reified T: AlertDialogFragment.Event> Observable<AlertDialogFragment.Event>.subscribeToEvent(
    subscriptions: CompositeDisposable,
    dialogTag: String,
    crossinline onNext: (T) -> Unit
) {
    subscribeToEvent<T>(subscriptions) { event ->
        if (event.dialogTag == dialogTag) {
            onNext(event)
        }
    }
}
