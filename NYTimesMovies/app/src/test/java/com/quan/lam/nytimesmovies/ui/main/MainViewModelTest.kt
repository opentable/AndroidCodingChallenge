package com.quan.lam.nytimesmovies.ui.main

import com.quan.lam.nytimesmovies.usecase.NYTimesMovieReviewsUseCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.quan.lam.nytimesmovies.usecase.BaseMovieReviewsUseCase
import org.junit.rules.TestRule
import org.junit.Rule
import org.mockito.Mockito.mock

/**
 * MainViewModel Test
 * Different action will be perform and we will test the final state of the ViewModel
 */
@RunWith(PowerMockRunner::class)
@PrepareForTest(NYTimesMovieReviewsUseCase::class, NYTimesMovieReviewsUseCase.Companion::class)
class MainViewModelTest {
    lateinit var testViewModel: MainViewModel
    lateinit var mockUseCase: NYTimesMovieReviewsUseCase
    @Rule
    private var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        PowerMockito.mockStatic(NYTimesMovieReviewsUseCase.Companion::class.java)
        mockUseCase = mock(NYTimesMovieReviewsUseCase::class.java)
        testViewModel = MainViewModel(mockUseCase)
    }

    @Test
    fun construction_test() {
        assert(MainViewModel(mockUseCase) is MainViewModel)
    }
    @Test
    fun getState_test() {
        assert(testViewModel.getState().value is MainViewModel.State)
    }

    @Test
    fun fetchMovieReviews_test() {
        testViewModel.fetchMovieReviews()
        assert(testViewModel.getState().value is MainViewModel.State.Loading)
        testViewModel.onFetchMoviesReviewResult(BaseMovieReviewsUseCase.Result.OnSuccess(ArrayList()))
        assert(testViewModel.getState().value is MainViewModel.State.ReviewsLoaded)
        testViewModel.onFetchMoviesReviewResult(BaseMovieReviewsUseCase.Result.OnError(Throwable()))
        assert(testViewModel.getState().value is MainViewModel.State.Error)
    }

    @Test
    fun fetchMoreMoviesReviews_test() {
        testViewModel.fetchMoreMovieReviews()
        assert(testViewModel.getState().value is MainViewModel.State.LoadingMore)
        testViewModel.onFetchMoviesReviewResult(BaseMovieReviewsUseCase.Result.OnSuccess(ArrayList()))
        assert(testViewModel.getState().value is MainViewModel.State.ReviewsLoadedMore)
        testViewModel.onFetchMoviesReviewResult(BaseMovieReviewsUseCase.Result.OnError(Throwable()))
        assert(testViewModel.getState().value is MainViewModel.State.Error)
    }

    @Test
    fun getReviewsList_test() {
        assert(testViewModel.getReviewsList() is ArrayList)
    }
}