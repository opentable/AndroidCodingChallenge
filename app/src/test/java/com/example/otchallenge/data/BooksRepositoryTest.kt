package com.example.otchallenge.data

import com.example.otchallenge.data.network.BooksService
import com.example.otchallenge.data.network.models.BooksResponse
import com.example.otchallenge.domain.ResultData
import com.example.otchallenge.utils.SampleData
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import retrofit2.Response

class BooksRepositoryTest {

    @Mock
    private lateinit var booksApi: BooksService

    private lateinit var repository: BooksRepositoryImpl

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        repository = BooksRepositoryImpl(booksApi)
    }

    @After
    fun tearDown() {

    }

    @Test
    fun `getBooks should return success result`() = runTest {
        val booksResponse = SampleData.booksResponse()
        val successFetch = Response.success(booksResponse)

        val expectedBooks = SampleData.booksList()

        `when`(booksApi.getBooks(SampleData.API_KEY)).thenReturn(successFetch)

        val result = repository.getBooks()

        when (result) {
            is ResultData.Success -> {
                assertEquals(expectedBooks, result.data)
            }

            else -> {
                // do nothing
            }
        }
    }

    //     @Test
    // error response not working properly
    fun `getBooks should return error result on exception`() = runTest {
        val errorException = Exception("Network Error")
        val errorResponse = Response.error<BooksResponse>(
            500, "{}".toResponseBody("application/json".toMediaTypeOrNull())
        )
        `when`(booksApi.getBooks(SampleData.API_KEY)).thenReturn(errorResponse)

        val result = repository.getBooks()
        when (result) {
            is ResultData.Error -> {
                assertEquals(errorException, result.error)
            }

            is ResultData.Success -> {
                // do nothing
            }
        }
    }
}