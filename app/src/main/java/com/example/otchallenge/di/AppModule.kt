package com.example.otchallenge.di

import android.app.Application
import android.content.Context
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.data.database.BookDatabase
import com.example.otchallenge.domain.executor.JobExecutor
import com.example.otchallenge.domain.executor.PostExecutionThread
import com.example.otchallenge.domain.executor.ThreadExecutor
import com.example.otchallenge.domain.executor.UIThread
import com.example.otchallenge.utils.ConnectivityChecker
import com.example.otchallenge.utils.NetworkHelper

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {
    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    @Singleton
    fun provideContext(): Context = application.applicationContext

    @Provides
    @Singleton
    fun provideConnectivityChecker(networkHelper: NetworkHelper): ConnectivityChecker =
        networkHelper

    @Provides
    @Singleton
    fun provideThreadExecutor(): ThreadExecutor {
        return JobExecutor()
    }

    @Provides
    @Singleton
    fun providePostExecutionThread(): PostExecutionThread {
        return UIThread()
    }
}