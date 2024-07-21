package com.example.otchallenge.presentation.extensions

import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.LoadStates

val CombinedLoadStates.error: Throwable?
    get() {
        return when {
            refresh is LoadState.Error -> (refresh as LoadState.Error).error
            append is LoadState.Error -> (append as LoadState.Error).error
            prepend is LoadState.Error -> (prepend as LoadState.Error).error
            else -> null
        }
    }

val CombinedLoadStates.isLoading: Boolean
    get() {
        return refresh is LoadState.Loading ||
               append  is LoadState.Loading ||
               prepend is LoadState.Loading
    }

val LoadStates.isLoading: Boolean
    get() {
        return refresh is LoadState.Loading ||
               append  is LoadState.Loading ||
               prepend is LoadState.Loading
    }
