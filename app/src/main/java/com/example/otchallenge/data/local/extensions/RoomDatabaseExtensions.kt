package com.example.otchallenge.data.local.extensions

import androidx.room.RoomDatabase
import io.reactivex.Maybe
import io.reactivex.Single
import java.util.concurrent.Callable

fun <T: Any> RoomDatabase.runInTransactionSingle(
    block: () -> T
): Single<T> {
    return Single.create { emitter ->
        try {
            emitter.onSuccess(
                runInTransaction(Callable { block() })
            )
        } catch(e: Throwable) {
            emitter.onError(e)
        }
    }
}

fun <T: Any> RoomDatabase.runInTransactionMaybe(
    block: () -> T?
): Maybe<T> {
    return Maybe.fromCallable {
        runInTransaction(Callable { block() })
    }
}