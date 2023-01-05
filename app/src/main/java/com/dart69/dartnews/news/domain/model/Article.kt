package com.dart69.dartnews.news.domain.model

import java.io.Serializable

interface Model

data class Article(
    val title: String,
    val content: String,
    val titleImageUrl: String,
    val sourceUrl: String,
    val byLine: String,
    val publishedDate: String,
    val id: Long = 0
) : Serializable, Model {
    companion object {
        val Default = Article("", "", "", "", "", "")
    }
}
