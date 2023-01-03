package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.data.Cache

/**
 * @param K -> search query type
 * @param T -> response type
 * */
interface DataSource<K, T> {
    suspend fun searchBy(key: K): List<T>?
}

interface RemoteDataSource<K, T> : DataSource<K, T>

/**
 * Create class Cache, which will save data in random access memory
 * by Key like Map. Also implement clean strategy when cache's data reach some memory limit.
 * Example: val cache = Cache<Key, DataType>(cleanStrategy = CleanStrategy.RemoveNotUsedData).
 *
 * Modify CachedDataSource key type(Period) to generic K. In case with MostPopularApi we must
 * cache data by both type and period.
 * val key = ArticlesKey(type = Type.MostViewed, period = Period.Day)
 * Example: cachedDataSource.cache(key, articles).
 * */
interface CachedDataSource<K, T> : DataSource<K, T> {
    suspend fun cache(key: K, data: List<T>)

    suspend fun clear(key: K)

    class Default<K, T>(
        private val cache: Cache<K, List<T>>
    ) : CachedDataSource<K, T> {

        override suspend fun cache(key: K, data: List<T>) {
            cache.save(key, data)
        }

        override suspend fun clear(key: K) {
            cache.clear(key)
        }

        override suspend fun searchBy(key: K): List<T>? =
            cache.load(key)
    }
}

