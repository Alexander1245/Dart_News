package com.dart69.dartnews.news.domain.repository

import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.domain.model.ArticleDetails
import com.dart69.dartnews.news.domain.model.ResultsFlow

interface ArticlesRepository : SearchingRepository<ArticleDetails, Article> {
    fun loadFavourites(): ResultsFlow<List<Article>>

    suspend fun addToFavourites(article: Article)

    suspend fun removeFromFavourites(article: Article)
}