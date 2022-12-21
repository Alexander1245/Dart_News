package com.dart69.dartnews

import com.dart69.dartnews.news.data.datasources.RemotePeriodicDataSource
import com.dart69.dartnews.news.domain.model.Period
import com.dart69.dartnews.news.domain.repository.ApiTestModel

internal class FakeRemoteDataSource: RemotePeriodicDataSource<ApiTestModel> {
    private val data = mapOf(
        Period.Day to listOf(ApiTestModel(1, "Title 1")),
        Period.Week to listOf(ApiTestModel(2, "Title 2")),
        Period.Month to listOf(ApiTestModel(3, "Title 3")),
    )

    override suspend fun loadByPeriod(period: Period): List<ApiTestModel> = data[period].orEmpty()
}