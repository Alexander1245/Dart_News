package com.dart69.dartnews.news.domain.model

data class ArticleDetails(
    val type: ArticlesType,
    val period: Period
) {
    companion object {
        val Default = ArticleDetails(ArticlesType.MostViewed, Period.Day)
    }
}
