package com.dart69.dartnews.news.data

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface Cache<K, T> {
    suspend fun save(key: K, value: T)

    suspend fun load(key: K): T?

    suspend fun clear(key: K)

    /**
     * Default Cache implementation, mutable operations (save, clear) safe from concurrent modifications.
     * */
    class Default<K, T> : Cache<K, T> {
        private val mutex = Mutex()
        private val cache = HashMap<K, T>()

        override suspend fun save(key: K, value: T) {
            mutex.withLock {
                cache += (key to value)
            }
        }

        override suspend fun load(key: K): T? =
            cache[key]

        override suspend fun clear(key: K) {
            mutex.withLock {
                cache -= key
            }
        }
    }
}