package com.quan.lam.nytimesmovies.ui.main

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModel
import com.quan.lam.nytimesmovies.usecase.BaseMovieReviewsUseCase

/**
 * Extension of ViewModelProvider.Factory to allow building ViewModel with custom parameters.
 */
class MovieReviewViewModelFactory : ViewModelProvider.Factory {
    private var mUseCase: BaseMovieReviewsUseCase

    constructor(useCase: BaseMovieReviewsUseCase) {
        mUseCase = useCase
    }
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieReviewListViewModel(mUseCase) as T
    }
}