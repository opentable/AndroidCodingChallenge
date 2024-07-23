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
			// This is only to show case that we need to hide apikey's, but I'm putting this fallback
			// to not make it hard to build the app (I read the README :))
			//.nyTimesApiKey(BuildConfig.nyTimesApiKey)
			.nyTimesApiKey("KoRB4K5LRHygfjCL2AH6iQ7NeUqDAGAB")
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
