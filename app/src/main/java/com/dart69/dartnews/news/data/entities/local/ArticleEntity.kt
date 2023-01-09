package com.dart69.dartnews.news.data.entities.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dart69.dartnews.news.data.mappers.RawModel

@Entity
data class ArticleEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val content: String,
    val titleImageUrl: String,
    val sourceUrl: String,
    val byLine: String,
    val publishedDate: String,
): RawModel