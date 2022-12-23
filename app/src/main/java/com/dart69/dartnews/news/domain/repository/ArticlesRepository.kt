package com.dart69.dartnews.news.domain.repository

import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.domain.model.Period

interface ArticlesRepository : PeriodicRepository<Article> {
    suspend fun hasLocalData(period: Period): Boolean
}