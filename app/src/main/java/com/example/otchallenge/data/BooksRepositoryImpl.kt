package com.example.otchallenge.data

import com.example.otchallenge.data.network.BooksService
import com.example.otchallenge.data.network.models.BooksResponse
import com.example.otchallenge.data.network.utils.getResultFrom
import com.example.otchallenge.domain.ResultData
import com.example.otchallenge.domain.entity.ItemBook
import com.example.otchallenge.domain.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

// this should be injected with a BuildConfig property, then, if whe have different flavors
// or config environment we can change this API key
const val API_KEY = "KoRB4K5LRHygfjCL2AH6iQ7NeUqDAGAB"

class BooksRepositoryImpl @Inject constructor(
    private val booksApi: BooksService,
) : BooksRepository {
    override suspend fun getBooks(): ResultData<List<ItemBook>> = withContext(Dispatchers.IO) {
        return@withContext try {
            when (val resultNetwork = getResultFrom(booksApi.getBooks(API_KEY))) {
                is ResultData.Success -> ResultData.Success(resultNetwork.data.toEntity())
                is ResultData.Error -> ResultData.Error(resultNetwork.error)
            }
        } catch (e: Exception) {
            ResultData.Error(e)
        }

    }
}

private fun BooksResponse.toEntity(): List<ItemBook> {
    return this.results?.books?.map {
        ItemBook(
            title = it?.title ?: "",
            author = it?.author ?: "",
            description = it?.author ?: "",
            bookImageUrl = it?.bookImage ?: "",
            amazonBuyUrl = it?.amazonProductUrl ?: "",
            isbn = it?.primaryIsbn10 ?: ""
        )
    } ?: listOf()
}
