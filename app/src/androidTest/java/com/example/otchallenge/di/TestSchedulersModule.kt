package com.example.otchallenge.di

import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Named

@Module
class TestSchedulersModule {

    @Provides
    @Named(SchedulersModule.IO)
    fun providesIOScheduler(): Scheduler {
        return Schedulers.trampoline()
    }

    @Provides
    @Named(SchedulersModule.Main)
    fun providesMainScheduler(): Scheduler {
        return Schedulers.trampoline()
    }
}