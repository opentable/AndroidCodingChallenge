package com.example.otchallenge.domain.mapper

import com.example.otchallenge.domain.model.Book
import com.example.otchallenge.presentation.model.BookDetailPresentation
import com.example.otchallenge.utils.Transform

class BookDetailMapper : Transform<Book, BookDetailPresentation>() {
    override fun transform(value: Book): BookDetailPresentation {
        return BookDetailPresentation(
            id = value.id,
            rank = value.rank,
            description = value.description,
            price = value.price,
            title = value.title,
            author = value.author,
            bookImage = value.bookImage,
            bookImageWidth = value.bookImageWidth,
            bookImageHeight = value.bookImageHeight,
            amazonProductUrl = value.amazonProductUrl
        )
    }
}