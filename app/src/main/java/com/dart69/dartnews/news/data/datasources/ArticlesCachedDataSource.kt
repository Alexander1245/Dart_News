package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.data.ArticlesCache
import com.dart69.dartnews.news.data.entities.ArticleResponse
import com.dart69.dartnews.news.domain.model.ArticleDetails
import javax.inject.Inject

private typealias BaseCachedDataSource = CachedDataSource<ArticleDetails, ArticleResponse>
private typealias Implementation = CachedDataSource.Default<ArticleDetails, ArticleResponse>

interface ArticlesCachedDataSource : CachedDataSource<ArticleDetails, ArticleResponse> {

    class Default @Inject constructor(
        cache: ArticlesCache,
    ) : ArticlesCachedDataSource, BaseCachedDataSource by Implementation(cache)
}