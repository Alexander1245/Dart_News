package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.data.entities.ArticleResponse
import com.dart69.dartnews.news.domain.model.Period
import com.dart69.dartnews.news.domain.model.AvailableDispatchers
import kotlinx.coroutines.withContext

interface ArticlesRemoteDataSource : RemotePeriodicDataSource<ArticleResponse> {

    class Default(
        private val api: MostViewedApi,
        private val apiKey: String,
        private val dispatchers: AvailableDispatchers,
    ) : ArticlesRemoteDataSource {
        override suspend fun loadByPeriod(period: Period): List<ArticleResponse> =
            withContext(dispatchers.io) {
                api.loadResponse(period.value, apiKey).body()?.results.orEmpty()
            }
    }
}

