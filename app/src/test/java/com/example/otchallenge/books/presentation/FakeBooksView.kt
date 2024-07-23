package com.example.otchallenge.books.presentation

class FakeBooksView: BooksView {

    var books = emptyList<BookUiModel>()
    var showLastModifiedDate = ""
    var showNoBooksFound = false
    var showTryAgainError = ""

    override fun showBooks(books: List<BookUiModel>) {
        this.books = books
    }

    override fun showLastModifiedDate(date: String) {
        if (books.isNotEmpty())
            showLastModifiedDate = date
    }

    override fun showNoBooksFound() {
        showNoBooksFound = true
    }

    override fun showTryAgainError(error: String) {
        showTryAgainError = error
    }

    override fun setLoading(isLoading: Boolean) {
    }
}