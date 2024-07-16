package com.example.otchallenge.data.paging

import androidx.paging.PagingSource

interface PagingSourceFactory<K: Any, T: Any> {

    operator fun invoke(): PagingSource<K, T>

}