package com.example.otchallenge.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NYTimesApiKey

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class BaseUrl