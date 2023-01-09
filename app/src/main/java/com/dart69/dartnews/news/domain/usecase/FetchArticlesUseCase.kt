package com.dart69.dartnews.news.domain.usecase

import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.domain.model.ArticleDetails
import com.dart69.dartnews.news.domain.model.ResultsFlow
import com.dart69.dartnews.news.domain.model.combineFlatten
import com.dart69.dartnews.news.domain.repository.ArticlesRepository
import com.dart69.dartnews.news.networking.ConnectionObserver
import com.dart69.dartnews.news.networking.ConnectionState
import com.dart69.dartnews.news.networking.connectionErrorResults
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface FetchArticlesUseCase {
    operator fun invoke(details: Flow<ArticleDetails>): ResultsFlow<List<Article>>

    class Default @Inject constructor(
        private val repository: ArticlesRepository,
        private val connectionObserver: ConnectionObserver,
    ) : FetchArticlesUseCase {

        override fun invoke(details: Flow<ArticleDetails>): ResultsFlow<List<Article>> {
            val connectionState = connectionObserver.observeConnectionState()
            return connectionState.combineFlatten(details) { state, key ->
                if (state == ConnectionState.Connected || repository.hasLocalData(key)) {
                    repository.searchBy(key)
                } else {
                    connectionErrorResults()
                }
            }
        }
    }
}