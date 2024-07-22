package com.example.otchallenge.domain.usecase

import com.example.otchallenge.domain.executor.PostExecutionThread
import com.example.otchallenge.domain.executor.ThreadExecutor
import io.reactivex.Single
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers

abstract class SingleUseCase<Params, T>(
    private val threadExecutor: ThreadExecutor,
    private val postExecutionThread: PostExecutionThread
) : UseCase<Params, Single<T>>() {

    /**
     * Executes the current use case
     */
    fun execute(params: Params? = null): Single<T> {
        return buildUseCase(params)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler())
    }

    /**
     * Executes the current use case
     *
     */
    fun execute(params: Params? = null, observer: DisposableSingleObserver<T>) {
        val single = buildUseCase(params)
            .subscribeOn(Schedulers.from(threadExecutor))
            .observeOn(postExecutionThread.getScheduler())
        addDisposable(single.subscribeWith(observer))
    }
}