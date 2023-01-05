package com.dart69.dartnews.news.domain.repository

import com.dart69.dartnews.news.domain.model.ResultsFlow

interface SearchingRepository<K, T> {
    fun searchBy(key: K): ResultsFlow<List<T>>

    suspend fun hasLocalData(key: K): Boolean
}