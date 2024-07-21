package com.example.otchallenge.utils

import com.example.otchallenge.data.database.BookEntity
import com.example.otchallenge.data.model.BookApi
import com.example.otchallenge.data.model.MockData
import org.junit.Assert.assertEquals
import org.junit.Test

class BookMapperTest {

    @Test
    fun `mapApiToEntity should correctly map BookApi to BookEntity`() {
        val bookApi = MockData.createMockBookApiList()[0]
        val bookEntity = BookMapper.mapApiToEntity(bookApi)

        assertEquals(bookApi.title, bookEntity.title)
        assertEquals(bookApi.author, bookEntity.author)
        assertEquals(bookApi.description, bookEntity.description)
        assertEquals(bookApi.bookImage, bookEntity.bookImage)
        assertEquals(bookApi.primaryIsbn10, bookEntity.primaryIsbn10)
        assertEquals(bookApi.primaryIsbn13, bookEntity.primaryIsbn13)
        assertEquals(bookApi.publisher, bookEntity.publisher)
        assertEquals(bookApi.price, bookEntity.price)
        assertEquals(bookApi.rank, bookEntity.rank)
        assertEquals(bookApi.rankLastWeek, bookEntity.rankLastWeek)
        assertEquals(bookApi.weeksOnList, bookEntity.weeksOnList)
        assertEquals(bookApi.contributor, bookEntity.contributor)
        assertEquals(bookApi.bookImageWidth, bookEntity.bookImageWidth)
        assertEquals(bookApi.bookImageHeight, bookEntity.bookImageHeight)
        assertEquals(bookApi.amazonProductUrl, bookEntity.amazonProductUrl)
        assertEquals(bookApi.ageGroup, bookEntity.ageGroup)
        assertEquals(bookApi.bookUri, bookEntity.bookUri)
    }

    @Test
    fun `mapApiToEntity should handle empty or default fields in BookApi`() {
        val bookApi = BookApi(
            rank = 1,
            rankLastWeek = 1,
            weeksOnList = 1,
            asterisk = 0,
            dagger = 0,
            primaryIsbn10 = "1234567890",
            primaryIsbn13 = "123-1234567890",
            publisher = "Publisher",
            description = "",
            price = "",
            title = "Title",
            author = "Author",
            contributor = "",
            contributorNote = "",
            bookImage = "",
            bookImageWidth = 0,
            bookImageHeight = 0,
            amazonProductUrl = "",
            ageGroup = "",
            bookReviewLink = "",
            firstChapterLink = "",
            sundayReviewLink = "",
            articleChapterLink = "",
            bookUri = ""
        )
        val bookEntity = BookMapper.mapApiToEntity(bookApi)

        assertEquals(bookApi.title, bookEntity.title)
        assertEquals(bookApi.author, bookEntity.author)
        assertEquals(bookApi.description, bookEntity.description)
        assertEquals(bookApi.bookImage, bookEntity.bookImage)
        assertEquals(bookApi.primaryIsbn10, bookEntity.primaryIsbn10)
        assertEquals(bookApi.primaryIsbn13, bookEntity.primaryIsbn13)
        assertEquals(bookApi.publisher, bookEntity.publisher)
        assertEquals(bookApi.price, bookEntity.price)
        assertEquals(bookApi.rank, bookEntity.rank)
        assertEquals(bookApi.rankLastWeek, bookEntity.rankLastWeek)
        assertEquals(bookApi.weeksOnList, bookEntity.weeksOnList)
        assertEquals(bookApi.contributor, bookEntity.contributor)
        assertEquals(bookApi.bookImageWidth, bookEntity.bookImageWidth)
        assertEquals(bookApi.bookImageHeight, bookEntity.bookImageHeight)
        assertEquals(bookApi.amazonProductUrl, bookEntity.amazonProductUrl)
        assertEquals(bookApi.ageGroup, bookEntity.ageGroup)
        assertEquals(bookApi.bookUri, bookEntity.bookUri)
    }

    @Test
    fun `mapEntityToDomain should correctly map BookEntity to Book`() {
        val bookEntity = MockData.createMockBookEntities()[0]
        val book = BookMapper.mapEntityToDomain(bookEntity)

        assertEquals(bookEntity.id, book.id)
        assertEquals(bookEntity.title, book.title)
        assertEquals(bookEntity.author, book.author)
        assertEquals(bookEntity.description, book.description)
        assertEquals(bookEntity.bookImage, book.bookImage)
        assertEquals(bookEntity.primaryIsbn10, book.primaryIsbn10)
        assertEquals(bookEntity.primaryIsbn13, book.primaryIsbn13)
        assertEquals(bookEntity.publisher, book.publisher)
        assertEquals(bookEntity.price, book.price)
        assertEquals(bookEntity.rank, book.rank)
        assertEquals(bookEntity.rankLastWeek, book.rankLastWeek)
        assertEquals(bookEntity.weeksOnList, book.weeksOnList)
        assertEquals(bookEntity.contributor, book.contributor)
        assertEquals(bookEntity.bookImageWidth, book.bookImageWidth)
        assertEquals(bookEntity.bookImageHeight, book.bookImageHeight)
        assertEquals(bookEntity.amazonProductUrl, book.amazonProductUrl)
        assertEquals(bookEntity.ageGroup, book.ageGroup)
        assertEquals(bookEntity.bookUri, book.bookUri)
    }

    @Test
    fun `mapEntityToDomain should handle empty or default fields in BookEntity`() {
        val bookEntity = BookEntity(
            id = 1,
            rank = 1,
            rankLastWeek = 1,
            weeksOnList = 1,
            primaryIsbn10 = "1234567890",
            primaryIsbn13 = "123-1234567890",
            publisher = "Publisher",
            description = "",
            price = "",
            title = "Title",
            author = "Author",
            contributor = "",
            bookImage = "",
            bookImageWidth = 0,
            bookImageHeight = 0,
            amazonProductUrl = "",
            ageGroup = "",
            bookUri = ""
        )
        val book = BookMapper.mapEntityToDomain(bookEntity)

        assertEquals(bookEntity.id, book.id)
        assertEquals(bookEntity.title, book.title)
        assertEquals(bookEntity.author, book.author)
        assertEquals(bookEntity.description, book.description)
        assertEquals(bookEntity.bookImage, book.bookImage)
        assertEquals(bookEntity.primaryIsbn10, book.primaryIsbn10)
        assertEquals(bookEntity.primaryIsbn13, book.primaryIsbn13)
        assertEquals(bookEntity.publisher, book.publisher)
        assertEquals(bookEntity.price, book.price)
        assertEquals(bookEntity.rank, book.rank)
        assertEquals(bookEntity.rankLastWeek, book.rankLastWeek)
        assertEquals(bookEntity.weeksOnList, book.weeksOnList)
        assertEquals(bookEntity.contributor, book.contributor)
        assertEquals(bookEntity.bookImageWidth, book.bookImageWidth)
        assertEquals(bookEntity.bookImageHeight, book.bookImageHeight)
        assertEquals(bookEntity.amazonProductUrl, book.amazonProductUrl)
        assertEquals(bookEntity.ageGroup, book.ageGroup)
        assertEquals(bookEntity.bookUri, book.bookUri)
    }

    @Test
    fun `mapEntityToDomainSummary should correctly map BookEntity to BookSummary`() {
        val bookEntity = MockData.createMockBookEntities()[0]
        val bookSummary = BookMapper.mapEntityToDomainSummary(bookEntity)

        assertEquals(bookEntity.id, bookSummary.id)
        assertEquals(bookEntity.title, bookSummary.title)
        assertEquals(bookEntity.description, bookSummary.description)
        assertEquals(bookEntity.bookImage, bookSummary.bookImage)
    }

    @Test
    fun `mapDomainSummaryToPresentation should correctly map BookSummary to BookPresentation`() {
        val bookSummary = MockData.createMockBookSummaries()[0]
        val bookPresentation = BookMapper.mapDomainSummaryToPresentation(bookSummary)

        assertEquals(bookSummary.id, bookPresentation.id)
        assertEquals(bookSummary.title, bookPresentation.title)
        assertEquals(bookSummary.description, bookPresentation.description)
        assertEquals(bookSummary.bookImage, bookPresentation.bookImage)
    }

    @Test
    fun `mapDomainToDetailPresentation should correctly map Book to BookDetailPresentation`() {
        val book = MockData.createMockBook()
        val bookDetailPresentation = BookMapper.mapDomainToDetailPresentation(book)

        assertEquals(book.id, bookDetailPresentation.id)
        assertEquals(book.rank, bookDetailPresentation.rank)
        assertEquals(book.description, bookDetailPresentation.description)
        assertEquals(book.price, bookDetailPresentation.price)
        assertEquals(book.title, bookDetailPresentation.title)
        assertEquals(book.author, bookDetailPresentation.author)
        assertEquals(book.bookImage, bookDetailPresentation.bookImage)
        assertEquals(book.bookImageWidth, bookDetailPresentation.bookImageWidth)
        assertEquals(book.bookImageHeight, bookDetailPresentation.bookImageHeight)
        assertEquals(book.amazonProductUrl, bookDetailPresentation.amazonProductUrl)
    }

    @Test
    fun `mapSummaryToDomain should correctly map BookSummaryEntity to Book`() {
        val bookSummaryEntity = MockData.createMockBookSummaryEntities()[0]
        val book = BookMapper.mapSummaryToDomain(bookSummaryEntity)

        assertEquals(bookSummaryEntity.id, book.id)
        assertEquals(bookSummaryEntity.title, book.title)
        assertEquals(bookSummaryEntity.description, book.description)
        assertEquals(bookSummaryEntity.bookImage, book.bookImage)
    }

    @Test
    fun `mapEntitySummaryToDomainSummary should correctly map BookSummaryEntity to BookSummary`() {
        val bookSummaryEntity = MockData.createMockBookSummaryEntities()[0]
        val bookSummary = BookMapper.mapEntitySummaryToDomainSummary(bookSummaryEntity)

        assertEquals(bookSummaryEntity.id, bookSummary.id)
        assertEquals(bookSummaryEntity.title, bookSummary.title)
        assertEquals(bookSummaryEntity.description, bookSummary.description)
        assertEquals(bookSummaryEntity.bookImage, bookSummary.bookImage)
    }
}