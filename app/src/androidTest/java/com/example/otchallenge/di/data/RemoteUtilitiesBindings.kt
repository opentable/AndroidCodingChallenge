package com.example.otchallenge.di.data

import com.example.otchallenge.RemoteServerExceptionFactory
import com.example.otchallenge.RetrofitHttpExceptionFactory
import dagger.Binds
import dagger.Module

@Module
abstract class RemoteUtilitiesBindings {

    @Binds
    abstract fun bindRemoteServerExceptionFactory(
        impl: RetrofitHttpExceptionFactory
    ): RemoteServerExceptionFactory

}