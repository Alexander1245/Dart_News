package com.dart69.dartnews.news.data

import com.dart69.dartnews.news.data.datasources.CachedPeriodicDataSource
import com.dart69.dartnews.news.data.datasources.RemotePeriodicDataSource
import com.dart69.dartnews.news.domain.model.*
import com.dart69.dartnews.news.domain.repository.PeriodicRepository
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**@param I -> raw model from dataSources or Api.
 * @param O -> domain model for your application requirements.
 * @param modelMapper -> map raw model to required domain model.
 * */
abstract class DefaultPeriodicRepository<I, O>(
    protected val remoteDataSource: RemotePeriodicDataSource<I>,
    protected val cachedDataSource: CachedPeriodicDataSource<I>,
    protected val modelMapper: (I) -> O,
    protected val dispatchers: AvailableDispatchers,
) : PeriodicRepository<O> {
    override suspend fun fetch(period: Period) {
        cachedDataSource.clear(period)
    }

    override fun observe(period: Period): ResultsFlow<List<O>> = resultsFlowOf {
        cachedDataSource.loadByPeriod(period).ifEmpty { remoteDataSource.loadByPeriod(period) }
    }.onEach { results ->
        if (results is Results.Completed) cachedDataSource.cache(period, results.data)
    }.map { results ->
        results.mapResults { data -> data.map(modelMapper) }
    }.flowOn(dispatchers.io)
}