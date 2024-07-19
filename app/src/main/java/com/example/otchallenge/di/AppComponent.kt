package com.example.otchallenge.di

import com.example.otchallenge.data.di.DatabaseModule
import com.example.otchallenge.data.di.NetworkModule
import com.example.otchallenge.data.di.RepositoryModule
import com.example.otchallenge.presentation.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, DatabaseModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(activity: MainActivity)
}
