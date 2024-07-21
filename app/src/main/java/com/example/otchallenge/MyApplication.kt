package com.example.otchallenge

import android.app.Application
import com.example.otchallenge.data.di.*
import com.example.otchallenge.di.*

class MyApplication : Application() {


    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            //.repositoryModule(RepositoryModule())
            .databaseModule(DatabaseModule())
            .presenterModule(PresenterModule())
            .useCaseModule(UseCaseModule())
            .build()
        appComponent.inject(this)
    }
}
