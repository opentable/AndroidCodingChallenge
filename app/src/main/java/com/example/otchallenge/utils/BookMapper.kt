package com.example.otchallenge.utils

import com.example.otchallenge.data.database.BookEntity
import com.example.otchallenge.data.database.BookSummaryEntity
import com.example.otchallenge.data.model.BookApi
import com.example.otchallenge.domain.model.Book
import com.example.otchallenge.domain.model.BookSummary
import com.example.otchallenge.presentation.model.BookDetailPresentation
import com.example.otchallenge.presentation.model.BookPresentation

object BookMapper {

    fun mapApiToDomain(apiModel: BookApi): Book {
        return Book(
            rank = apiModel.rank,
            rankLastWeek = apiModel.rankLastWeek,
            weeksOnList = apiModel.weeksOnList,
            primaryIsbn10 = apiModel.primaryIsbn10,
            primaryIsbn13 = apiModel.primaryIsbn13,
            publisher = apiModel.publisher,
            description = apiModel.description,
            price = apiModel.price,
            title = apiModel.title,
            author = apiModel.author,
            contributor = apiModel.contributor,
            bookImage = apiModel.bookImage,
            bookImageWidth = apiModel.bookImageWidth,
            bookImageHeight = apiModel.bookImageHeight,
            amazonProductUrl = apiModel.amazonProductUrl,
            ageGroup = apiModel.ageGroup,
            bookUri = apiModel.bookUri
        )
    }

    fun mapApiToEntity(apiModel: BookApi): BookEntity {
        return BookEntity(
            title = apiModel.title,
            author = apiModel.author,
            description = apiModel.description,
            bookImage = apiModel.bookImage,
            primaryIsbn10 = apiModel.primaryIsbn10,
            primaryIsbn13 = apiModel.primaryIsbn13,
            publisher = apiModel.publisher,
            price = apiModel.price,
            rank = apiModel.rank,
            rankLastWeek = apiModel.rankLastWeek,
            weeksOnList = apiModel.weeksOnList,
            contributor = apiModel.contributor,
            bookImageWidth = apiModel.bookImageWidth,
            bookImageHeight = apiModel.bookImageHeight,
            amazonProductUrl = apiModel.amazonProductUrl,
            ageGroup = apiModel.ageGroup,
            bookUri = apiModel.bookUri.toString()
        )
    }

    fun mapEntityToDomain(entity: BookEntity): Book {
        return Book(
            id = entity.id,
            title = entity.title,
            author = entity.author,
            description = entity.description,
            bookImage = entity.bookImage,
            primaryIsbn10 = entity.primaryIsbn10,
            primaryIsbn13 = entity.primaryIsbn13,
            publisher = entity.publisher,
            price = entity.price,
            rank = entity.rank,
            rankLastWeek = entity.rankLastWeek,
            weeksOnList = entity.weeksOnList,
            contributor = entity.contributor,
            bookImageWidth = entity.bookImageWidth,
            bookImageHeight = entity.bookImageHeight,
            amazonProductUrl = entity.amazonProductUrl,
            ageGroup = entity.ageGroup,
            bookUri = entity.bookUri
        )
    }

    fun mapEntityToDomainSummary(summary: BookEntity): BookSummary {
        return BookSummary(
            id = summary.id,
            title = summary.title,
            description = summary.description,
            bookImage = summary.bookImage
        )
    }

    fun mapDomainSummaryToPresentation(domain: BookSummary): BookPresentation {
        return BookPresentation(
            id = domain.id,
            title = domain.title,
            description = domain.description,
            bookImage = domain.bookImage
        )
    }

    fun mapDomainToDetailPresentation(domain: Book): BookDetailPresentation {
        return BookDetailPresentation(
            id = domain.id,
            rank = domain.rank,
            description = domain.description,
            price = domain.price,
            title = domain.title,
            author = domain.author,
            bookImage = domain.bookImage,
            bookImageWidth = domain.bookImageWidth,
            bookImageHeight = domain.bookImageHeight,
            amazonProductUrl = domain.amazonProductUrl
        )
    }

    fun mapSummaryToDomain(summary: BookSummaryEntity): Book {
        return Book(
            id = summary.id,
            title = summary.title,
            author = "",
            description = summary.description,
            bookImage = summary.bookImage,
            primaryIsbn10 = "",
            primaryIsbn13 = "",
            publisher = "",
            price = "",
            rank = 0,
            rankLastWeek = 0,
            weeksOnList = 0,
            contributor = "",
            bookImageWidth = 0,
            bookImageHeight = 0,
            amazonProductUrl = "",
            ageGroup = "",
            bookUri = ""
        )
    }

    fun mapEntitySummaryToDomainSummary(entity: BookSummaryEntity): BookSummary {
        return BookSummary(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            bookImage = entity.bookImage
        )
    }
}