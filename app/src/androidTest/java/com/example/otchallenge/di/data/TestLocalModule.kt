package com.example.otchallenge.di.data

import android.content.Context
import androidx.room.Room
import com.example.otchallenge.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestLocalModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        context: Context
    ): AppDatabase {
        return Room.inMemoryDatabaseBuilder(
            context = context,
            klass = AppDatabase::class.java
        ).build()
    }

}