package com.dart69.dartnews.news.data

import com.dart69.dartnews.news.data.entities.ArticleResponse
import com.dart69.dartnews.news.domain.model.ArticleDetails
import javax.inject.Inject

private typealias BaseCache = Cache<ArticleDetails, List<ArticleResponse>>
private typealias CacheImplementation = Cache.Default<ArticleDetails, List<ArticleResponse>>

interface ArticlesCache: BaseCache {

    class Default @Inject constructor(): ArticlesCache, BaseCache by CacheImplementation()
}