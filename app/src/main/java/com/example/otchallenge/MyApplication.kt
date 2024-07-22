package com.example.otchallenge

import android.app.Application
import com.example.otchallenge.di.AppComponent
import com.example.otchallenge.di.AppModule
import com.example.otchallenge.di.DaggerAppComponent

class MyApplication : Application() {


    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory()
            .create(AppModule(this))
        appComponent.inject(this)
        /*appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            //.repositoryModule(RepositoryModule())
            .databaseModule(DatabaseModule())
            .presenterModule(PresenterModule())
            .useCaseModule(UseCaseModule())
            .build()
        appComponent.inject(this)*/
    }
}
