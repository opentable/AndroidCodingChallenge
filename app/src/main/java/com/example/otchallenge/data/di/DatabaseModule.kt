package com.example.otchallenge.data.di

import android.app.Application
import androidx.room.Room
import com.example.otchallenge.data.database.BookDao
import com.example.otchallenge.data.database.BookDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application): BookDatabase {
        return Room.databaseBuilder(application, BookDatabase::class.java, "books.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideBookDao(database: BookDatabase): BookDao {
        return database.bookDao()
    }
}