package com.example.otchallenge.di.data

import androidx.paging.PagingConfig
import com.example.otchallenge.data.remote.NYTApiConstants
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class PagingConfigModule {

    @Provides
    @Named(BookList)
    fun provideBookListPagingConfig(): PagingConfig {
        return PagingConfig(
            pageSize = NYTApiConstants.BookList.PageSize,
            prefetchDistance = NYTApiConstants.BookList.PageSize / 4,
            initialLoadSize = NYTApiConstants.BookList.PageSize
        )
    }

    companion object {
        const val BookList = "book-list-paging-config"
    }

}