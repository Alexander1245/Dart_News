package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.data.Cache
import com.dart69.dartnews.news.data.entities.ArticleResponse
import com.dart69.dartnews.news.domain.model.ArticleDetails

interface ArticlesCachedDataSource : CachedDataSource<ArticleDetails, ArticleResponse> {

    class Default(
        cache: Cache<ArticleDetails, List<ArticleResponse>> = Cache.Default(),
        dataSource: CachedDataSource<ArticleDetails, ArticleResponse> = CachedDataSource.Default(cache),
    ) : ArticlesCachedDataSource, CachedDataSource<ArticleDetails, ArticleResponse> by dataSource
}