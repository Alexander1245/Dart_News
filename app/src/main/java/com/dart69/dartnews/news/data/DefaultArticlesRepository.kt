package com.dart69.dartnews.news.data

import com.dart69.dartnews.news.data.datasources.ArticlesCachedDataSource
import com.dart69.dartnews.news.data.datasources.ArticlesRemoteDataSource
import com.dart69.dartnews.news.data.entities.ArticleResponse
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.domain.model.ArticleDetails
import com.dart69.dartnews.news.domain.repository.ArticlesRepository
import com.dart69.dartnews.news.domain.repository.Repository
import com.dart69.dartnews.news.other.AvailableDispatchers

private typealias BaseRepository = DefaultRepository<ArticleDetails, ArticleResponse, Article>

private val mapper = { response: ArticleResponse ->
    Article(
        title = response.title,
        content = response.abstract,
        titleImageUrl = response.media.firstOrNull()?.`media-metadata`
            ?.maxBy {
                it.height * it.width
            }?.url.orEmpty(),
        sourceUrl = response.url,
        byLine = response.byline,
        publishedDate = response.published_date
    )
}

class DefaultArticlesRepository(
    remoteDataSource: ArticlesRemoteDataSource,
    cachedDataSource: ArticlesCachedDataSource,
    dispatchers: AvailableDispatchers,
    modelMapper: (ArticleResponse) -> Article = mapper,
) : ArticlesRepository, Repository<ArticleDetails, Article> by BaseRepository(
    remoteDataSource,
    cachedDataSource,
    modelMapper,
    dispatchers
)