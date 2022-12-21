package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.domain.model.Period
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertArrayEquals
import org.junit.Before
import org.junit.Test

internal data class TestModel(
    val id: Long,
    val title: String,
    val content: String,
)

inline fun <reified T> assertCollectionsEquals(expected: Collection<T>, actual: Collection<T>) {
    assertArrayEquals(expected.toTypedArray(), actual.toTypedArray())
}

internal class CachedPeriodicDataSourceTest {
    private lateinit var data: List<TestModel>
    private lateinit var dataSource: CachedPeriodicDataSource<TestModel>

    @Before
    fun before() {
        data = List(100) {
            TestModel(it.toLong(), "Title #$it", "Content #$it")
        }
        dataSource = object : CachedPeriodicDataSource.Default<TestModel>() {}
    }

    @Test
    fun cache() = runBlocking {
        val periodIterator = Period.values().iterator()
        repeat(3) {
            val from = it * 5
            val to = from + 5
            val period = periodIterator.next()
            val expected = data.subList(from, to)
            dataSource.cache(period, expected)
            assertCollectionsEquals(expected, dataSource.loadByPeriod(period))
        }

    }

    @Test
    fun loadByPeriod() = runBlocking {
        cache()
        val expected = data.subList(0, 15)
        val actual = Period.values().map { dataSource.loadByPeriod(it) }.flatten()
        assertCollectionsEquals(expected, actual)
    }
}