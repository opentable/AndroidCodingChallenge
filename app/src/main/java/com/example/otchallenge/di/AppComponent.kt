package com.example.otchallenge.di

import com.example.otchallenge.MainActivity
import com.example.otchallenge.di.data.PagingConfigModule
import com.example.otchallenge.di.data.PagingSourceFactoryModule
import com.example.otchallenge.di.data.PresenterModule
import com.example.otchallenge.di.data.RemoteModule
import com.example.otchallenge.di.data.RepositoryModule
import com.example.otchallenge.presentation.BookListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
	modules = [
		SchedulersModule::class,
		RemoteModule::class,
		PagingConfigModule::class,
		PagingSourceFactoryModule::class,
		RepositoryModule::class,
		PresenterModule::class
	]
)
interface AppComponent {
	fun inject(activity: MainActivity)
	fun inject(fragment: BookListFragment)
}
