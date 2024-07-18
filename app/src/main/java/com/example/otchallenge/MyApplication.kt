package com.example.otchallenge

import com.example.otchallenge.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class MyApplication : DaggerApplication() {

	override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
		return DaggerAppComponent
			.builder()
			.applicationContext(this)
			.build()
	}

}
