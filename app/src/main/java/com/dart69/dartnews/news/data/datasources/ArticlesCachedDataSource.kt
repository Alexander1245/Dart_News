package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.data.entities.ArticleResponse

interface ArticlesCachedDataSource : CachedPeriodicDataSource<ArticleResponse> {

    class Default : CachedPeriodicDataSource.Default<ArticleResponse>(), ArticlesCachedDataSource
}