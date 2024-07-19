package com.example.otchallenge.di

import com.example.otchallenge.presentation.view.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component
interface AppComponent {
	fun inject(activity: MainActivity)
}
