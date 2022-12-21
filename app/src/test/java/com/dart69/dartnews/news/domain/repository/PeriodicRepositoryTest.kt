package com.dart69.dartnews.news.domain.repository

import com.dart69.dartnews.FakeRemoteDataSource
import com.dart69.dartnews.TestDispatchers
import com.dart69.dartnews.news.data.datasources.CachedPeriodicDataSource
import com.dart69.dartnews.news.data.datasources.RemotePeriodicDataSource
import com.dart69.dartnews.news.data.datasources.TestModel
import com.dart69.dartnews.news.domain.model.Period
import com.dart69.dartnews.news.domain.model.Results
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

data class ApiTestModel(
    val id: Long,
    val title: String,
)

private typealias BaseRepository = PeriodicRepository.Default<ApiTestModel, TestModel>

internal class PeriodicRepositoryTest {
    private val dispatchers = TestDispatchers()
    private val mapper = { from: ApiTestModel ->
        TestModel(from.id, from.title, from.title + from.title + from.id)
    }
    private lateinit var remotePeriodicDataSource: RemotePeriodicDataSource<ApiTestModel>
    private lateinit var cachedPeriodicDataSource: CachedPeriodicDataSource<ApiTestModel>
    private lateinit var repository: BaseRepository

    @Before
    fun before() {
        remotePeriodicDataSource = FakeRemoteDataSource()
        cachedPeriodicDataSource = object : CachedPeriodicDataSource.Default<ApiTestModel>() {}
        repository = object : BaseRepository(
            remotePeriodicDataSource,
            cachedPeriodicDataSource,
            mapper,
            dispatchers
        ) {}
    }

    @Test
    fun loadByPeriod() = runBlocking {
        val monthResults = async {
            repository.loadByPeriod(Period.Month).toList()
        }
        val dayResults = async {
            repository.loadByPeriod(Period.Day).toList()
        }
        assertTrue(monthResults.await().first() is Results.Loading)
        assertTrue(monthResults.await().component2() is Results.Completed)
        assertTrue(dayResults.await().first() is Results.Loading)
        assertTrue(dayResults.await().component2() is Results.Completed)
    }
}