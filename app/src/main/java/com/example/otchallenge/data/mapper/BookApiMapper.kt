package com.example.otchallenge.data.mapper

import com.example.otchallenge.data.database.BookEntity
import com.example.otchallenge.data.model.BookApi
import com.example.otchallenge.utils.Transform

class BookApiMapper : Transform<BookApi, BookEntity>() {
    override fun transform(value: BookApi): BookEntity {
        return BookEntity(
            title = value.title,
            author = value.author,
            description = value.description,
            bookImage = value.bookImage,
            primaryIsbn10 = value.primaryIsbn10,
            primaryIsbn13 = value.primaryIsbn13,
            publisher = value.publisher,
            price = value.price,
            rank = value.rank,
            rankLastWeek = value.rankLastWeek,
            weeksOnList = value.weeksOnList,
            contributor = value.contributor,
            bookImageWidth = value.bookImageWidth,
            bookImageHeight = value.bookImageHeight,
            amazonProductUrl = value.amazonProductUrl,
            ageGroup = value.ageGroup,
            bookUri = value.bookUri.toString()
        )
    }
}