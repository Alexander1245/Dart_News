package com.dart69.dartnews.news.data

import com.dart69.dartnews.TestDispatchers
import com.dart69.dartnews.news.data.datasources.CachedDataSource
import com.dart69.dartnews.news.data.datasources.RemoteDataSource
import com.dart69.dartnews.news.data.mappers.RawModel
import com.dart69.dartnews.news.domain.model.Model
import com.dart69.dartnews.news.domain.model.Results
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

internal data class TestModel(val text: String): Model

internal class FakeRemoteDataSource : RemoteDataSource<String, StringRawModel> {
    private val data = List(100) {
        "data $it"
    }.chunked(10).associateBy { testModels -> testModels.first().split(" ").last() }

    override suspend fun searchBy(key: String): List<StringRawModel>? = data[key]?.map { StringRawModel(it) }
}

@JvmInline
internal value class StringRawModel(val string: String): RawModel

internal class DefaultRepositoryTest {
    private lateinit var remote: RemoteDataSource<String, StringRawModel>
    private lateinit var cached: CachedDataSource<String, StringRawModel>
    private lateinit var modelMapper: (StringRawModel) -> TestModel
    private lateinit var repository: DefaultRepository<String, StringRawModel, TestModel>

    @Before
    fun beforeEach() {
        val dispatchers = TestDispatchers()
        remote = FakeRemoteDataSource()
        cached = CachedDataSource.Default(Cache.Default())
        modelMapper = { rawModel: StringRawModel -> TestModel(rawModel.string) }
        repository = DefaultRepository(remote, cached, modelMapper, dispatchers)
    }

    @Test
    fun fetch() = runBlocking {
    }

    @Test
    fun observe() = runBlocking {
        repeat(10) { index ->
            val key = (index * 10).toString()
            val expected = remote.searchBy(key)!!.map(modelMapper)
            val results = async {
                repository.searchBy(key).dropWhile { it !is Results.Completed }
                    .first() as Results.Completed
            }
            val actual = results.await().data
            assertCollectionsEquals(expected, actual)
        }
    }

    @Test
    fun hasLocalData() = runBlocking {
        val job1 =
            launch { repository.searchBy("10").dropWhile { it !is Results.Completed }.collect() }
        val job2 =
            launch { repository.searchBy("0").dropWhile { it !is Results.Completed }.collect() }
        joinAll(job1, job2)
        assertFalse(repository.hasLocalData("30"))
        assertTrue(repository.hasLocalData("0"))
        assertTrue(repository.hasLocalData("10"))
    }
}