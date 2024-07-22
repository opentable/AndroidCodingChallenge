package com.example.otchallenge.domain.mapper

import com.example.otchallenge.data.database.BookSummaryEntity
import com.example.otchallenge.domain.model.BookSummary
import com.example.otchallenge.utils.Transform

class BookSummaryEntityMapper : Transform<BookSummary, BookSummaryEntity>() {

    override fun transform(value: BookSummary): BookSummaryEntity {
        return BookSummaryEntity(
            id = value.id,
            title = value.title,
            description = value.description,
            bookImage = value.bookImage
        )
    }

    override fun reverseTransform(value: BookSummaryEntity): BookSummary {
        return BookSummary(
            id = value.id,
            title = value.title,
            description = value.description,
            bookImage = value.bookImage
        )
    }
}