package com.example.otchallenge.data.paging.booklist

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.otchallenge.data.entity.Book
import com.example.otchallenge.data.remote.BooksApi
import com.example.otchallenge.data.remote.mappers.toBookEntityList
import com.example.otchallenge.data.remote.mappers.wrappedError
import io.reactivex.Scheduler
import io.reactivex.Single


class BookListPagingSource (
    private val listName: String,
    private val booksApi: BooksApi,
    private val ioScheduler: Scheduler
) : RxPagingSource<Int, Book>() {

    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, Book>> {

        val currentKey = params.key ?: 0

        return booksApi.getBookList(
            name = listName,
            offset = currentKey
        )
            .map { response ->
                response.toBookEntityList()
            }
            .map { books->
                books.toLoadResult(currentKey, params.loadSize)
            }
            .onErrorReturn { error ->
                LoadResult.Error(error.wrappedError)
            }
            .subscribeOn(ioScheduler)
    }

    override fun getRefreshKey(state: PagingState<Int, Book>): Int? {
        return state.anchorPosition
            ?.let { anchor ->
                state.closestPageToPosition(anchor)
            }
            .let { page ->
                page?.prevKey?.plus(state.config.pageSize) ?: page?.nextKey?.minus(state.config.pageSize)
            }
    }

    private fun List<Book>.toLoadResult(key: Int, loadSize: Int): LoadResult<Int, Book> {

        val prevKey: Int? = if (key == 0) null else key - loadSize
        val nextKey: Int? = if (size < loadSize) null else key + loadSize

        return LoadResult.Page(
            data = this,
            prevKey = prevKey,
            nextKey = nextKey
        )
    }

}