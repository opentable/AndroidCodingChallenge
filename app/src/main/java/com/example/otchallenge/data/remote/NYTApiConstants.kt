package com.example.otchallenge.data.remote

object NYTApiConstants {

    const val BaseUrl = "https://api.nytimes.com"

    val ApiKey = Pair("api-key", "KoRB4K5LRHygfjCL2AH6iQ7NeUqDAGAB")

    object BookList {
        const val StartOffset = 0
        const val PageSize = 20
        const val CurrentDate = "current"
    }
}