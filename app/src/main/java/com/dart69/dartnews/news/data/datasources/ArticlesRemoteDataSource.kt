package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.data.entities.ArticleResponse
import com.dart69.dartnews.news.domain.model.ArticleDetails
import com.dart69.dartnews.news.di.AvailableDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface ArticlesRemoteDataSource : RemoteDataSource<ArticleDetails, ArticleResponse> {

    class Default @Inject constructor(
        private val responseFactory: ResponseFactory,
        private val dispatchers: AvailableDispatchers,
    ) : ArticlesRemoteDataSource {

        override suspend fun searchBy(key: ArticleDetails): List<ArticleResponse>? =
            withContext(dispatchers.io) {
                responseFactory.create(key).body()?.results
            }
    }
}

