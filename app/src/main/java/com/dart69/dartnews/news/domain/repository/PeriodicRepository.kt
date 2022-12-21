package com.dart69.dartnews.news.domain.repository

import com.dart69.dartnews.news.data.datasources.CachedPeriodicDataSource
import com.dart69.dartnews.news.data.datasources.RemotePeriodicDataSource
import com.dart69.dartnews.news.domain.model.*
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

interface PeriodicRepository<T> {
    suspend fun loadByPeriod(period: Period): ResultsFlow<List<T>>

    /**@param I -> raw model from dataSources or Api.
     * @param O -> domain model for your application requirements.
     * @param modelMapper -> map raw model to required domain model.
     * */
    abstract class Default<I, O>(
        private val remoteDataSource: RemotePeriodicDataSource<I>,
        private val cachedDataSource: CachedPeriodicDataSource<I>,
        private val modelMapper: (I) -> O,
        private val dispatchers: AvailableDispatchers,
    ) : PeriodicRepository<O> {
        override suspend fun loadByPeriod(period: Period): ResultsFlow<List<O>> =
            resultsFlowOf {
                remoteDataSource.loadByPeriod(period)
            }.onEach { results ->
                if (results is Results.Completed) cachedDataSource.cache(period, results.data)
            }.map { results ->
                results.mapResults { articles -> articles.map(modelMapper) }
            }.flowOn(dispatchers.io)
    }
}