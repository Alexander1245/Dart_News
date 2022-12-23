package com.dart69.dartnews.news.domain.repository

import com.dart69.dartnews.news.domain.model.Period
import com.dart69.dartnews.news.domain.model.ResultsFlow

interface PeriodicRepository<T> {
    suspend fun fetch(period: Period)

    fun observe(period: Period): ResultsFlow<List<T>>
}