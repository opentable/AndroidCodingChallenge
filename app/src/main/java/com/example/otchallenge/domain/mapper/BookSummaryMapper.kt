package com.example.otchallenge.domain.mapper

import com.example.otchallenge.domain.model.BookSummary
import com.example.otchallenge.presentation.model.BookPresentation
import com.example.otchallenge.utils.Transform

class BookSummaryMapper : Transform<BookSummary, BookPresentation>() {

    override fun transform(value: BookSummary): BookPresentation {
        return BookPresentation(
            id = value.id,
            title = value.title,
            description = value.description,
            bookImage = value.bookImage
        )
    }
}