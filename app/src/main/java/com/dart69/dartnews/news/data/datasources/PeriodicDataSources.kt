package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.domain.model.Period
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

interface PeriodicDataSource<T> {
    suspend fun loadByPeriod(period: Period): List<T>
}

interface RemotePeriodicDataSource<T> : PeriodicDataSource<T>

interface CachedPeriodicDataSource<T> : PeriodicDataSource<T> {
    suspend fun cache(period: Period, data: List<T>)

    suspend fun clear(period: Period)

    abstract class Default<T> : CachedPeriodicDataSource<T> {
        private val dataHolder = MutableStateFlow(mapOf<Period, List<T>>())

        override suspend fun cache(period: Period, data: List<T>) {
            dataHolder.update { byPeriod ->
                byPeriod.plus(period to data)
            }
        }

        override suspend fun clear(period: Period) {
            dataHolder.update { byPeriod ->
                byPeriod.minus(period)
            }
        }

        override suspend fun loadByPeriod(period: Period): List<T> =
            dataHolder.value[period].orEmpty()
    }
}

