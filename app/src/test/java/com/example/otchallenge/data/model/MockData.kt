package com.example.otchallenge.data.model

import com.example.otchallenge.data.database.BookEntity
import com.example.otchallenge.data.database.BookSummaryEntity
import com.example.otchallenge.domain.model.Book
import com.example.otchallenge.domain.model.BookSummary
import com.example.otchallenge.presentation.model.BookDetailPresentation

object MockData {
    fun createMockOverviewResponse(): OverviewResponse {
        return OverviewResponse(
            status = "OK",
            copyright = "Copyright (c) 2023 The New York Times Company. All Rights Reserved.",
            numResults = 10,
            results = createMockResults()
        )
    }

    private fun createMockResults(): Results {
        return Results(
            listName = "Hardcover Fiction",
            listNameEncoded = "hardcover-fiction",
            bestsellersDate = "2023-06-01",
            publishedDate = "2023-06-10",
            publishedDateDescription = "latest",
            nextPublishedDate = "",
            previousPublishedDate = "2023-06-03",
            displayName = "Hardcover Fiction",
            normalListEndsAt = 10,
            updated = "WEEKLY",
            books = createMockBookApiList(),
            corrections = null
        )
    }

    fun createMockBookApiList(): List<BookApi> {
        val books = mutableListOf<BookApi>()
        for (i in 1..10) {
            books.add(
                BookApi(
                    rank = i,
                    rankLastWeek = i - 1,
                    weeksOnList = 1,
                    asterisk = 0,
                    dagger = 0,
                    primaryIsbn10 = "123456789$i",
                    primaryIsbn13 = "123-123456789$i",
                    publisher = "Publisher $i",
                    description = "Description of book $i",
                    price = "$10.99",
                    title = "Title $i",
                    author = "Author $i",
                    contributor = "Contributor $i",
                    contributorNote = "Contributor Note $i",
                    bookImage = "https://example.com/book$i.jpg",
                    bookImageWidth = 128,
                    bookImageHeight = 192,
                    amazonProductUrl = "https://amazon.com/book$i",
                    ageGroup = "Age Group $i",
                    bookReviewLink = "https://review.com/book$i",
                    firstChapterLink = "https://firstchapter.com/book$i",
                    sundayReviewLink = "https://sundayreview.com/book$i",
                    articleChapterLink = "https://articlechapter.com/book$i",
                    bookUri = "bookUri$i"
                )
            )
        }
        return books
    }

    fun createMockBookEntities(): List<BookEntity> {
        val books = mutableListOf<BookEntity>()
        for (i in 1..10) {
            books.add(
                BookEntity(
                    id = i,
                    rank = i,
                    rankLastWeek = i - 1,
                    weeksOnList = 1,
                    primaryIsbn10 = "123456789$i",
                    primaryIsbn13 = "123-123456789$i",
                    publisher = "Publisher $i",
                    description = "Description of book $i",
                    price = "$10.99",
                    title = "Title $i",
                    author = "Author $i",
                    contributor = "Contributor $i",
                    bookImage = "https://example.com/book$i.jpg",
                    bookImageWidth = 128,
                    bookImageHeight = 192,
                    amazonProductUrl = "https://amazon.com/book$i",
                    ageGroup = "Age Group $i",
                    bookUri = "bookUri$i"
                )
            )
        }
        return books
    }

    fun createMockBookSummaryEntities(): List<BookSummaryEntity> {
        val summaries = mutableListOf<BookSummaryEntity>()
        for (i in 1..10) {
            summaries.add(
                BookSummaryEntity(
                    id = i,
                    title = "Title $i",
                    description = "Description of book $i",
                    bookImage = "https://example.com/book$i.jpg"
                )
            )
        }
        return summaries
    }

    fun createMockBookSummaries(): List<BookSummary> {
        val summaries = mutableListOf<BookSummary>()
        for (i in 1..10) {
            summaries.add(
                BookSummary(
                    id = i,
                    title = "Title $i",
                    description = "Description of book $i",
                    bookImage = "https://example.com/book$i.jpg"
                )
            )
        }
        return summaries
    }

    fun getMockSummary(): BookSummary {
        return BookSummary(
            id = 1,
            title = "Title 1",
            description = "Description of book 1",
            bookImage = "https://example.com/book1.jpg"
        )
    }

    fun createMockBook(): Book {
        return Book(
            id = 1,
            title = "Title 1",
            author = "Author 1",
            description = "Description 1",
            bookImage = "ImageURL1",
            rank = 1,
            rankLastWeek = 1,
            weeksOnList = 1,
            primaryIsbn10 = "1234567891",
            primaryIsbn13 = "123-1234567891",
            publisher = "Publisher 1",
            price = "$10.99",
            bookImageWidth = 128,
            bookImageHeight = 192,
            amazonProductUrl = "https://amazon.com/book1",
            ageGroup = "Age Group 1",
            bookUri = "bookUri1",
            contributor = "Some Contributor"
        )
    }

    fun getMockBookEntity(): BookEntity {
        return BookEntity(
            id = 1,
            rank = 1,
            rankLastWeek = 1,
            weeksOnList = 10,
            primaryIsbn10 = "1234567890",
            primaryIsbn13 = "1234567890123",
            publisher = "Publisher",
            description = "Description",
            price = "20.00",
            title = "Title",
            author = "Author",
            contributor = "Contributor",
            bookImage = "http://image.url",
            bookImageWidth = 200,
            bookImageHeight = 300,
            amazonProductUrl = "http://amazon.url",
            ageGroup = "Age Group",
            bookUri = "http://book.uri"
        )
    }

    fun getMockBookDomain(): Book {
        return Book(
            id = 1,
            rank = 1,
            rankLastWeek = 1,
            weeksOnList = 10,
            primaryIsbn10 = "1234567890",
            primaryIsbn13 = "1234567890123",
            publisher = "Publisher",
            description = "Description",
            price = "20.00",
            title = "Title",
            author = "Author",
            contributor = "Contributor",
            bookImage = "http://image.url",
            bookImageWidth = 200,
            bookImageHeight = 300,
            amazonProductUrl = "http://amazon.url",
            ageGroup = "Age Group",
            bookUri = "http://book.uri"
        )
    }

    fun getBookDetailPresentation(): BookDetailPresentation {
        return BookDetailPresentation(
            id = 1,
            title = "Title 1",
            author = "Author 1",
            description = "Description 1",
            bookImage = "ImageURL1",
            rank = 1,
            price = "$10.99",
            bookImageWidth = 128,
            bookImageHeight = 192,
            amazonProductUrl = "https://amazon.com/book1"
        )
    }
}