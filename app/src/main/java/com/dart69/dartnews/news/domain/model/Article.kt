package com.dart69.dartnews.news.domain.model

import java.io.Serializable

data class Article(
    val title: String,
    val content: String,
    val titleImageUrl: String,
    val sourceUrl: String,
    val byLine: String,
    val publishedDate: String,
) : Serializable {
    companion object {
        val Default = Article("", "", "", "", "", "")
    }
}
