package com.example.otchallenge.data.paging.booklist

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.rxjava2.RxRemoteMediator
import com.example.otchallenge.data.local.AppDatabase
import com.example.otchallenge.data.local.dao.composite.BookListPaginationDao
import com.example.otchallenge.data.local.entities.BookEntity
import com.example.otchallenge.data.paging.booklist.mappers.getBookEntities
import com.example.otchallenge.data.paging.booklist.mappers.getBookListEntity
import com.example.otchallenge.data.paging.booklist.mappers.getBookListEntryBookEntities
import com.example.otchallenge.data.paging.booklist.mappers.getBookListEntryEntity
import com.example.otchallenge.data.remote.BooksApi
import com.example.otchallenge.data.remote.NYTApiConstants
import com.example.otchallenge.data.remote.mappers.wrappedError
import com.example.otchallenge.di.SchedulersModule
import io.reactivex.Scheduler
import io.reactivex.Single
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalPagingApi::class)
abstract class BookListMediator : RxRemoteMediator<Int, BookEntity>() {

    protected lateinit var options: Options

    override fun initializeSingle(): Single<InitializeAction> {
        require(::options.isInitialized) {
            "Options in BookListMediator not set. Please set options using withParams(Options)"
        }
        return super.initializeSingle()
    }

    fun withOptions(options: Options): BookListMediator = apply {
        this.options = options
    }

    sealed interface Options {
        val bookListId: String

        data class LoadCurrent(override val bookListId: String) : Options
        data class LoadDate(override val bookListId: String, val date: LocalDate): Options
    }
}

class BookListMediatorImpl @Inject constructor(
    private val booksApi: BooksApi,
    private val bookListPaginationDao: BookListPaginationDao,
    @Named(SchedulersModule.IO)
    private val ioScheduler: Scheduler,
    appDatabase: AppDatabase
) : BookListMediator() {

    private val bookListsDao = appDatabase.bookListsDao()
    private val bookListRemoteKeysDao = appDatabase.bookListRemoteKeysDao()

    @OptIn(ExperimentalPagingApi::class)
    override fun loadSingle(
        loadType: LoadType,
        state: PagingState<Int, BookEntity>
    ): Single<MediatorResult> {
        return when(loadType) {
            LoadType.REFRESH -> getRefreshSingle(state)
            // Refreshing always starts from first page
            LoadType.PREPEND -> endOfPaginationSingle()
            LoadType.APPEND -> getAppendSingle()
        }.subscribeOn(ioScheduler).wrapErrors()
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun getRefreshSingle(
        state: PagingState<Int, BookEntity>
    ): Single<MediatorResult> {
        return Single.fromCallable {
            when (val options = this.options) {
                is Options.LoadCurrent -> NYTApiConstants.BookList.CurrentDate
                is Options.LoadDate -> options.date.toString()
            }
        }.flatMap { date ->
            booksApi.getBookList(
                name = options.bookListId,
                date = date,
                offset = NYTApiConstants.BookList.StartOffset
            )
        }.flatMap { response ->
            bookListPaginationDao.upsertListEntry(
                bookList = response.getBookListEntity(),
                entry = response.getBookListEntryEntity(),
                entryBooks = response.getBookListEntryBookEntities(),
                books = response.getBookEntities(),
                nextKeyFactory = { _, totalCount ->
                    state.config.pageSize
                        .takeIf { response.results.normalListEndsAt > totalCount }
                }
            ).map { totalCount ->
                MediatorResult.Success(
                    endOfPaginationReached = totalCount == response.results.normalListEndsAt
                )
            }.toSingle(
                MediatorResult.Success(endOfPaginationReached = true)
            )
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun getAppendSingle(): Single<MediatorResult> {

        fun getDate(): Single<LocalDate> {
            return when (val options = this.options) {
                is Options.LoadCurrent -> {
                    bookListsDao.getByIdMaybe(options.bookListId)
                        .map { bookList ->
                            bookList.lastEntryDate
                        }
                        .toSingle()
                }
                is Options.LoadDate -> {
                    Single.just(options.date)
                }
            }
        }

        return getDate()
            .flatMap { date ->
                bookListRemoteKeysDao.findMaybe(
                    bookListId = options.bookListId,
                    date = date
                ).toSingle()
            }
            .flatMap { remoteKey ->
                if (remoteKey.nextKey == null) {
                    return@flatMap endOfPaginationSingle()
                }
                booksApi.getBookList(
                    name = options.bookListId,
                    date = remoteKey.date.toString(),
                    offset = remoteKey.nextKey
                )
                .flatMap { response ->
                    bookListPaginationDao.appendEntryBooks(
                        entry = response.getBookListEntryEntity(),
                        entryBooks = response.getBookListEntryBookEntities(),
                        books = response.getBookEntities(),
                        nextKeyFactory = { _, totalCount ->
                            totalCount
                                .takeIf { response.results.normalListEndsAt > totalCount }
                        }
                    ).map { totalCount ->
                        MediatorResult.Success(
                            endOfPaginationReached = totalCount == response.results.normalListEndsAt
                        )
                    }.toSingle(
                        MediatorResult.Success(endOfPaginationReached = true)
                    )
                }
            }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun Single<MediatorResult>.wrapErrors(): Single<MediatorResult> {
        return onErrorReturn { error ->
            MediatorResult.Error(error.wrappedError)
        }
    }

    @OptIn(ExperimentalPagingApi::class)
    private fun endOfPaginationSingle(): Single<MediatorResult> {
        return Single.just(MediatorResult.Success(endOfPaginationReached = true))
    }

}
