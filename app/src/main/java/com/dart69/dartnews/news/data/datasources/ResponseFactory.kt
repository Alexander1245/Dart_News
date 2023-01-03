package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.data.entities.NytResponse
import com.dart69.dartnews.news.domain.model.ArticleDetails
import com.dart69.dartnews.news.domain.model.ArticlesType
import retrofit2.Response

interface ResponseFactory {

    suspend fun create(key: ArticleDetails): Response<NytResponse>

    class Default(
        private val apiKey: String,
        private val mostPopularApi: MostPopularApi,
    ) : ResponseFactory {
        override suspend fun create(key: ArticleDetails): Response<NytResponse> {
            val period = key.period.value
            return when (key.type) {
                ArticlesType.MostViewed -> mostPopularApi.loadMostViewed(period, apiKey)
                ArticlesType.MostEmailed -> mostPopularApi.loadMostEmailed(period, apiKey)
                ArticlesType.MostShared -> mostPopularApi.loadMostShared(period, apiKey)
            }
        }
    }
}