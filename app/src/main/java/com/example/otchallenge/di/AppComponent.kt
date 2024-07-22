package com.example.otchallenge.di

import com.example.otchallenge.MyApplication
import com.example.otchallenge.data.di.DatabaseModule
import com.example.otchallenge.data.di.NetworkModule
import com.example.otchallenge.data.di.RepositoryModule
import com.example.otchallenge.presentation.view.BookDetailFragment
import com.example.otchallenge.presentation.view.BookListFragment
import com.example.otchallenge.presentation.view.MainActivity
import com.example.otchallenge.presentation.view.SplashFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        NetworkModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        PresenterModule::class,
        UseCaseModule::class
    ]
)
interface AppComponent {
    fun inject(application: MyApplication)
    fun inject(fragment: SplashFragment)
    fun inject(fragment: BookListFragment)
    fun inject(fragment: BookDetailFragment)
    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(appModule: AppModule): AppComponent
    }
}
