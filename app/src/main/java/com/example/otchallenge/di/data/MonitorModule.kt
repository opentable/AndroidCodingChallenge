package com.example.otchallenge.di.data

import android.content.Context
import com.example.otchallenge.data.monitor.createInternetConnectionMonitor
import dagger.Module
import dagger.Provides
import io.reactivex.Observable
import javax.inject.Named
import javax.inject.Singleton

@Module
class MonitorModule {

    @Provides
    @Singleton
    @Named(INTERNET_CONNECTION)
    fun provideInternetConnectionMonitor(
        context: Context
    ): Observable<Boolean> {
        return createInternetConnectionMonitor(context)
    }

    companion object {
        const val INTERNET_CONNECTION = "InternetConnectionMonitor"
    }
}