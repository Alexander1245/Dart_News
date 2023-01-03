package com.dart69.dartnews.news.data

import com.dart69.dartnews.news.data.datasources.CachedDataSource
import com.dart69.dartnews.news.data.datasources.RemoteDataSource
import com.dart69.dartnews.news.domain.model.Results
import com.dart69.dartnews.news.domain.model.ResultsFlow
import com.dart69.dartnews.news.domain.model.mapResults
import com.dart69.dartnews.news.domain.model.resultsFlowOf
import com.dart69.dartnews.news.domain.repository.Repository
import com.dart69.dartnews.news.other.AvailableDispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * @param K -> key type
 * @param I -> raw model from dataSources or Api.
 * @param O -> domain model for your application requirements.
 * @param modelMapper -> map raw model to required domain model.
 * */
class DefaultRepository<K, I, O>(
    private val remoteDataSource: RemoteDataSource<K, I>,
    private val cachedDataSource: CachedDataSource<K, I>,
    private val modelMapper: (I) -> O,
    private val dispatchers: AvailableDispatchers,
) : Repository<K, O> {

    override fun searchBy(key: K): ResultsFlow<List<O>> = resultsFlowOf {
        cachedDataSource.searchBy(key) ?: remoteDataSource.searchBy(key)
    }.onEach { results ->
        if (results is Results.Completed && results.data != null) cachedDataSource.cache(key, results.data)
    }.map { results ->
        results.mapResults { data -> data?.map(modelMapper).orEmpty() }
    }.flowOn(dispatchers.io)

    override suspend fun hasLocalData(key: K): Boolean =
        cachedDataSource.searchBy(key) != null
}
