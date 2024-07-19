package com.example.otchallenge.di

import android.app.Application
import android.content.Context
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
}