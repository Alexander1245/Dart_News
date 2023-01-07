package com.dart69.dartnews.news.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

interface Model

@Parcelize
data class Article(
    override val id: Long,
    val title: String,
    val content: String,
    val titleImageUrl: String,
    val sourceUrl: String,
    val byLine: String,
    val publishedDate: String,
    val isSelected: Boolean = false,
) : Identifiable, Model, Parcelable {
    companion object {
        val Default = Article(0, "", "", "", "", "", "")
    }
}
