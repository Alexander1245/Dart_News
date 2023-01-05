package com.dart69.dartnews.news.data

import com.dart69.dartnews.news.data.datasources.ArticlesCachedDataSource
import com.dart69.dartnews.news.data.datasources.ArticlesRemoteDataSource
import com.dart69.dartnews.news.data.datasources.db.ArticlesDao
import com.dart69.dartnews.news.data.entities.ArticleResponse
import com.dart69.dartnews.news.data.mappers.ArticleEntityMapper
import com.dart69.dartnews.news.data.mappers.ArticleResponseMapper
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.domain.model.ArticleDetails
import com.dart69.dartnews.news.domain.model.ResultsFlow
import com.dart69.dartnews.news.domain.model.resultsFlowOf
import com.dart69.dartnews.news.domain.repository.ArticlesRepository
import com.dart69.dartnews.news.domain.repository.SearchingRepository
import com.dart69.dartnews.news.other.AvailableDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

private typealias BaseRepository = SearchingRepository<ArticleDetails, Article>
private typealias RepositoryImplementation = DefaultRepository<ArticleDetails, ArticleResponse, Article>

class DefaultArticlesRepository @Inject constructor(
    private val remoteSource: ArticlesRemoteDataSource,
    private val cachedSource: ArticlesCachedDataSource,
    private val localSource: ArticlesDao,
    private val responseMapper: ArticleResponseMapper,
    private val entityMapper: ArticleEntityMapper,
    private val dispatchers: AvailableDispatchers,
) : ArticlesRepository, BaseRepository by RepositoryImplementation(
    remoteSource, cachedSource, responseMapper, dispatchers
) {

    override fun loadFavourites(): ResultsFlow<List<Article>> = resultsFlowOf {
        localSource.loadAll().orEmpty().map(entityMapper::toModel)
    }

    override suspend fun removeFromFavourites(article: Article) = withContext(dispatchers.io) {
        localSource.clear(entityMapper.toRawModel(article))
    }

    override suspend fun addToFavourites(article: Article) = withContext(dispatchers.io) {
        localSource.insert(entityMapper.toRawModel(article))
    }
}