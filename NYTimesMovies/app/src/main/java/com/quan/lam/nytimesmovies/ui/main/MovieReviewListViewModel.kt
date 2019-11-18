package com.quan.lam.nytimesmovies.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.quan.lam.nytimesmovies.usecase.BaseMovieReviewsUseCase

/**
 * MovieReviewListViewModel
 * @param moviesReviewsUseCase: The source of the Movie Reviews data
 * Having the data source as a parameter allow for switch data source on demand.
 */
class MovieReviewListViewModel(private var moviesReviewsUseCase: BaseMovieReviewsUseCase) : ViewModel() {

    //State of Movie Review List Fragment
    //Loading and ReviewLoaded represent the initial fetch of data
    //LoadingMore and ReviewLoadedMore represent additional fetch of data

    sealed class State {
        object Uninitialized: State()
        object ReviewsLoaded : State()
        object Loading : State()
        data class LoadingMore(val offset: Int): State()
        data class ReviewsLoadedMore(val offset: Int) : State()
        data class Error(val throwable: Throwable) : State()
    }

    private val state by lazy { MediatorLiveData<State>() }
    //Fetched reviews will be store in this list
    private val reviewList by lazy { ArrayList<MovieReviewListItem> () }
    init {
        state.value = State.Uninitialized
        //The Fragment State will observe
        state.addSource(moviesReviewsUseCase.getLiveData(), ::onFetchMoviesReviewResult)
    }

    override fun onCleared() {
        moviesReviewsUseCase.cleanUp()
    }

    /**
     * Initial Fetch
     */
    fun fetchMovieReviews() {
        state.value = State.Loading
        moviesReviewsUseCase.execute()
    }

    /**
     * Additional Fetch
     */
    fun fetchMoreMovieReviews() {
        //Pass the offset value to the fetch call
        state.value = State.LoadingMore(offset = reviewList.size)
        moviesReviewsUseCase.execute(offset = reviewList.size)
    }

    fun getState(): LiveData<State> = state

    fun getReviewsList() = reviewList

    fun getItemCount(): Int = reviewList.size

    /**
     * On Fetch result returned
     * @return Result of the data fetch, either Success or Error
     */
    fun onFetchMoviesReviewResult(result: BaseMovieReviewsUseCase.Result?) {
        when (result) {
            is BaseMovieReviewsUseCase.Result.OnSuccess -> {
                reviewList.addAll(result.result.map { MovieReviewListItem(it) })
                when (state.value) {
                    is State.Loading -> {
                        state.value = State.ReviewsLoaded
                    }
                    is State.LoadingMore -> {
                        //Pass the offset value to the State object in order for the view to
                        //react accordingly
                        val offset = (state.value as State.LoadingMore).offset
                        state.value = State.ReviewsLoadedMore(offset)
                    }
                }
            }
            //Pass the error to the view to display if needed
            is BaseMovieReviewsUseCase.Result.OnError -> state.value = State.Error(result.throwable)
        }
    }
}
