package com.example.otchallenge.di

import android.content.Context
import com.example.otchallenge.data.paging.BookListMediatorLoadSingleErrorTest
import com.example.otchallenge.data.paging.BookListMediatorLoadSingleTest
import com.example.otchallenge.di.data.PagingConfigModule
import com.example.otchallenge.di.data.RemoteMediatorModule
import com.example.otchallenge.di.data.RemoteUtilitiesBindings
import com.example.otchallenge.di.data.TestLocalModule
import com.example.otchallenge.di.data.TestRemoteModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        TestSchedulersModule::class,
        TestLocalModule::class,
        TestRemoteModule::class,
        PagingConfigModule::class,
        RemoteMediatorModule::class,
        RemoteUtilitiesBindings::class,
        PagingConfigModule::class,
    ]
)
interface PagingSourceTestComponent : AppComponent {

    fun inject(test: BookListMediatorLoadSingleTest)
    fun inject(test: BookListMediatorLoadSingleErrorTest)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun applicationContext(context: Context): Builder
        fun build(): PagingSourceTestComponent
    }
}
