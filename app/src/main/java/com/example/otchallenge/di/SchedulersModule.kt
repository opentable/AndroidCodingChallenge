package com.example.otchallenge.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class SchedulersModule {

    @Provides
    @Named(IO)
    fun providesIOScheduler(): Scheduler {
        return Schedulers.io()
    }

    @Provides
    @Named(Main)
    fun providesMainScheduler(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    companion object {
        const val IO = "Schedulers-IO"
        const val Main = "Schedulers-Main"
    }
}