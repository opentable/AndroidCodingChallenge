package com.example.otchallenge.di

import com.example.otchallenge.MainActivity
import com.example.otchallenge.di.data.PagingModule
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
		PagingModule::class,
		RepositoryModule::class,
		PresenterModule::class
	]
)
interface AppComponent {
	fun inject(activity: MainActivity)
	fun inject(fragment: BookListFragment)
}
