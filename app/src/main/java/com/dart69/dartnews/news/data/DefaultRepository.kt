package com.dart69.dartnews.news.data

import com.dart69.dartnews.news.data.datasources.CachedDataSource
import com.dart69.dartnews.news.data.datasources.RemoteDataSource
import com.dart69.dartnews.news.data.mappers.ModelMapper
import com.dart69.dartnews.news.data.mappers.RawModel
import com.dart69.dartnews.news.domain.model.*
import com.dart69.dartnews.news.domain.repository.SearchingRepository
import com.dart69.dartnews.news.di.AvailableDispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

/**
 * @param K -> key type
 * @param I -> raw model from dataSources or Api.
 * @param O -> domain model for your application requirements.
 * @param modelMapper -> map raw model to required domain model.
 * */
class DefaultRepository<K, I : RawModel, O : Model>(
    private val remoteDataSource: RemoteDataSource<K, I>,
    private val cachedDataSource: CachedDataSource<K, I>,
    private val modelMapper: ModelMapper<I, O>,
    private val dispatchers: AvailableDispatchers,
) : SearchingRepository<K, O> {

    override fun searchBy(key: K): ResultsFlow<List<O>> = resultsFlowOf {
        cachedDataSource.searchBy(key) ?: remoteDataSource.searchBy(key)
    }.onEach { results ->
        if (results is Results.Completed && results.data != null) cachedDataSource.save(
            key,
            results.data
        )
    }.map { results ->
        results.mapResults { data -> data?.map(modelMapper::toModel).orEmpty() }
    }.flowOn(dispatchers.io)

    override suspend fun hasLocalData(key: K): Boolean =
        cachedDataSource.searchBy(key) != null
}
