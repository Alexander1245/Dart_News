package com.dart69.dartnews.news.data

import com.dart69.dartnews.news.data.datasources.ArticlesCachedDataSource
import com.dart69.dartnews.news.data.datasources.ArticlesRemoteDataSource
import com.dart69.dartnews.news.data.entities.ArticleResponse
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.domain.model.AvailableDispatchers
import com.dart69.dartnews.news.domain.model.Period
import com.dart69.dartnews.news.domain.repository.ArticlesRepository
import kotlinx.coroutines.withContext

private typealias BaseRepository = DefaultPeriodicRepository<ArticleResponse, Article>

private val mapper = { response: ArticleResponse ->
    Article(
        title = response.title,
        content = response.abstract,
        byLine = response.byline,
        publishDate = response.published_date,
        titleImageUrl = response.media.firstOrNull()?.`media-metadata`?.firstOrNull()?.url.orEmpty()
    )
}

class DefaultArticlesRepository(
    remoteDataSource: ArticlesRemoteDataSource,
    cachedDataSource: ArticlesCachedDataSource,
    dispatchers: AvailableDispatchers,
    modelMapper: (ArticleResponse) -> Article = mapper,
) : BaseRepository(remoteDataSource, cachedDataSource, modelMapper, dispatchers),
    ArticlesRepository {
    override suspend fun hasLocalData(period: Period): Boolean = withContext(dispatchers.io) {
        cachedDataSource.loadByPeriod(period).isNotEmpty()
    }
}