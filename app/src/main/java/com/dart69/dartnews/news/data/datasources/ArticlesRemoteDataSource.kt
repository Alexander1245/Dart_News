package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.data.entities.ArticleResponse
import com.dart69.dartnews.news.domain.model.ArticleDetails
import com.dart69.dartnews.news.domain.model.ArticlesType
import com.dart69.dartnews.news.other.AvailableDispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

interface ArticlesRemoteDataSource : RemoteDataSource<ArticleDetails, ArticleResponse> {
    suspend fun changeArticlesType(newType: ArticlesType)

    class Default(
        private val responseFactory: ResponseFactory,
        private val dispatchers: AvailableDispatchers,
    ) : ArticlesRemoteDataSource {
        private val type = MutableStateFlow(ArticlesType.MostViewed)

        override suspend fun changeArticlesType(newType: ArticlesType) =
            type.emit(newType)

        override suspend fun searchBy(key: ArticleDetails): List<ArticleResponse>? =
            withContext(dispatchers.io) {
                responseFactory.create(key).body()?.results
            }
    }
}

