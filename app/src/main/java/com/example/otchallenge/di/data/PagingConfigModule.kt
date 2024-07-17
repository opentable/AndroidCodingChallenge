package com.example.otchallenge.di.data

import androidx.paging.PagingConfig
import com.example.otchallenge.data.remote.NYTApiSettings
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class PagingConfigModule {

    @Provides
    @Named(BookList)
    fun provideBookListPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = NYTApiSettings.BookList.PageSize,
            prefetchDistance = NYTApiSettings.BookList.PageSize / 4,
            initialLoadSize = NYTApiSettings.BookList.PageSize
        )
    }

    companion object {
        const val BookList = "book-list-paging-config"
    }

}