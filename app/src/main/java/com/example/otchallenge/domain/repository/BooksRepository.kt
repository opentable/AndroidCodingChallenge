package com.example.otchallenge.domain.repository

import com.example.otchallenge.domain.ResultData
import com.example.otchallenge.domain.entity.ItemBook

interface BooksRepository {

    suspend fun getBooks(): ResultData<List<ItemBook>>
}