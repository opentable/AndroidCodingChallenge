package com.example.otchallenge.di

import com.example.otchallenge.data.paging.BookListPagingSourceTest
import com.example.otchallenge.di.data.PagingConfigModule
import com.example.otchallenge.di.data.PagingSourceFactoryModule
import com.example.otchallenge.di.data.RemoteUtilitiesBindings
import com.example.otchallenge.di.data.TestRemoteModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        TestSchedulersModule::class,
        TestRemoteModule::class,
        RemoteUtilitiesBindings::class,
        PagingConfigModule::class,
        PagingSourceFactoryModule::class,
    ]
)
interface PagingSourceTestComponent {
    fun inject(test: BookListPagingSourceTest)

    companion object {
        val instance: PagingSourceTestComponent by lazy {
            DaggerPagingSourceTestComponent.builder().build()
        }
    }
}
