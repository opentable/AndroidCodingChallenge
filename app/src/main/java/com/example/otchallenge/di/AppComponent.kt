package com.example.otchallenge.di

import android.content.Context
import com.example.otchallenge.di.data.MonitorModule
import com.example.otchallenge.di.data.PagingConfigModule
import com.example.otchallenge.di.data.PagingSourceFactoryModule
import com.example.otchallenge.di.data.RemoteModule
import com.example.otchallenge.di.data.RepositoryModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
	modules = [
		SchedulersModule::class,
		MonitorModule::class,
		RemoteModule::class,
		PagingConfigModule::class,
		PagingSourceFactoryModule::class,
		RepositoryModule::class,
		PresenterModule::class,
		FragmentModule::class,
		ActivityModule::class,
		AndroidSupportInjectionModule::class
	]
)
interface AppComponent : AndroidInjector<DaggerApplication> {

	override fun inject(application: DaggerApplication)

	@Component.Builder
	interface Builder {
		@BindsInstance
		fun applicationContext(context: Context): Builder
		fun build(): AppComponent
	}
}
