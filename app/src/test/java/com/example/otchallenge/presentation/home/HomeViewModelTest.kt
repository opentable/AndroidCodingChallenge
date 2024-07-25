package com.example.otchallenge.presentation.home

import com.example.otchallenge.domain.ResultData
import com.example.otchallenge.domain.entity.ItemBook
import com.example.otchallenge.domain.usecase.GetBooksUseCase
import com.example.otchallenge.utils.SampleData
import com.example.otchallenge.utils.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Test


class HomeViewModelTest {
    // TODO Mock dependencies and config test coroutine scope
    private lateinit var getBooksUseCase: GetBooksUseCase
    private lateinit var viewModel: HomeViewModelImpl

    @Test
    fun `toggleViewTypeList should toggle between CAROUSEL and VERTICAL`() = runTest {
        val initialState = ViewTypeList.CAROUSEL
        viewModel.toggleViewTypeList(initialState)

        // TODO assert with ViewTypeList.VERTICAL
        viewModel.viewTypeListState.getOrAwaitValue()

        val newState = ViewTypeList.VERTICAL
        viewModel.toggleViewTypeList(newState)

        // TODO assert with ViewTypeList.CAROUSEL
        viewModel.viewTypeListState.getOrAwaitValue()
    }

    @Test
    fun `getBooks should post books list on success`() = runTest {
        val books = SampleData.booksList()
        val successResult = ResultData.Success(books)

        // TODO mock getBookUserCase to return successResult
        getBooksUseCase.invoke()

        viewModel.getBooks()

        // TODO assert with false
        viewModel.isLoading.getOrAwaitValue()
        // TODO assert is equals to books sample data
        viewModel.bookList.getOrAwaitValue()
    }

    @Test
    fun `getBooks should handle error result`() = runTest {
        val errorResult = ResultData.Error(Exception("Test Error"))

        // TODO mock getBookUserCase to return errorResult
        getBooksUseCase.invoke()

        viewModel.getBooks()

        // TODO assert with false
        viewModel.isLoading.getOrAwaitValue()
        // TODO assert is empty or null
        viewModel.bookList.getOrAwaitValue()
    }
}