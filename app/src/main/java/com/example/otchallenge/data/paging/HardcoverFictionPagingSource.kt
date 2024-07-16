package com.example.otchallenge.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.data.remote.BooksApi
import com.example.otchallenge.data.remote.mappers.toBookEntityList
import com.example.otchallenge.di.SchedulersModule
import io.reactivex.Scheduler
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named

class HardCoverFictionPagingSourceFactory @Inject constructor(
    private val bookListApi: BooksApi,
    @Named(SchedulersModule.IO)
    private val ioScheduler: Scheduler
): PagingSourceFactory<Int, Book> {
    override fun invoke(): PagingSource<Int, Book> {
        return HardcoverFictionPagingSource(
            bookListApi = bookListApi,
            ioScheduler = ioScheduler
        )
    }
}

class HardcoverFictionPagingSource (
    private val bookListApi: BooksApi,
    private val ioScheduler: Scheduler
) : RxPagingSource<Int, Book>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Book>> {

        val currentKey = params.key ?: 0

        return bookListApi.getHardcoverFiction(offset = currentKey)
            .map { response ->
                response.toBookEntityList()
            }
            .map { books->
                books.toLoadResult(currentKey)
            }
            .onErrorReturn { error ->
                LoadResult.Error(error)
            }
            .subscribeOn(ioScheduler)
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return state.anchorPosition
            ?.let { anchor ->
                state.closestPageToPosition(anchor)
            }
            .let { page ->
                page?.prevKey?.plus(20) ?: page?.nextKey?.minus(20)
            }
    }

    private fun List<Book>.toLoadResult(key: Int): LoadResult<Int, Book> {
        val prevKey = if (key == 0) null else key - 20
        val nextKey = if (size < 20) null else key + 20
        return LoadResult.Page(
            data = this,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

}