package com.example.otchallenge

import android.app.Application
import com.example.otchallenge.di.AppComponent
import com.example.otchallenge.di.DaggerAppComponent
import timber.log.Timber

class MyApplication : Application() {

	lateinit var appComponent: AppComponent

	override fun onCreate() {
		super.onCreate()
		appComponent = DaggerAppComponent
			.builder()
			.baseUrl("https://api.nytimes.com/")
			.context(this)
			.nyTimesApiKey(BuildConfig.nyTimesApiKey)
			.build()

		setupTimber()
	}

	private fun setupTimber() {
		if (BuildConfig.DEBUG) {
			Timber.plant(Timber.DebugTree())
		} else {
			//plant production analytics
		}
	}
}
