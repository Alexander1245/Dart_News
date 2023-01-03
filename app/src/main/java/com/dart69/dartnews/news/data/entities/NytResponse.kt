package com.dart69.dartnews.news.data.entities

data class NytResponse(
    val copyright: String,
    val num_results: Int,
    val results: List<ArticleResponse>,
    val status: String
)