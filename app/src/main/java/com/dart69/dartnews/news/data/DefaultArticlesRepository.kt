package com.dart69.dartnews.news.data

import com.dart69.dartnews.news.data.datasources.ArticlesCachedDataSource
import com.dart69.dartnews.news.data.datasources.ArticlesRemoteDataSource
import com.dart69.dartnews.news.data.entities.ArticleResponse
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.domain.model.AvailableDispatchers
import com.dart69.dartnews.news.domain.repository.ArticlesRepository
import com.dart69.dartnews.news.domain.repository.PeriodicRepository

private typealias BaseRepository = PeriodicRepository.Default<ArticleResponse, Article>

class DefaultArticlesRepository(
    remoteDataSource: ArticlesRemoteDataSource,
    cachedDataSource: ArticlesCachedDataSource,
    modelMapper: (ArticleResponse) -> Article,
    dispatchers: AvailableDispatchers,
) : BaseRepository(remoteDataSource, cachedDataSource, modelMapper, dispatchers), ArticlesRepository