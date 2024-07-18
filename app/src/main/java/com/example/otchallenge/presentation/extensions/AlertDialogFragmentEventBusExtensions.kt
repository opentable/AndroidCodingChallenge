package com.example.otchallenge.presentation.extensions

import com.example.otchallenge.presentation.components.AlertDialogFragment
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable

fun Observable<AlertDialogFragment.Event>.subscribeToButtonClickEvents(
    subscriptions: CompositeDisposable,
    onNext: (AlertDialogFragment.Event.Click) -> Unit
) {
    subscribe(subscriptions) { event ->
        if (event is AlertDialogFragment.Event.Click) {
            onNext(event)
        }
    }
}
fun Observable<AlertDialogFragment.Event>.subscribeToButtonClickEvents(
    subscriptions: CompositeDisposable,
    dialogTag: String,
    onNext: (AlertDialogFragment.Event.Click) -> Unit
) {
    subscribeToButtonClickEvents(subscriptions) { event ->
        if (event.dialog.tag == dialogTag) {
            onNext(event)
        }
    }
}
