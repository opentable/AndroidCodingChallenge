package com.example.otchallenge.di.data

import android.content.Context
import androidx.room.Room
import com.example.otchallenge.data.local.AppDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalModule {

    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context): AppDatabase {
        return Room
            .databaseBuilder(
                context = context,
                klass = AppDatabase::class.java,
                name = "app-database"
            )
            .build()
    }

}