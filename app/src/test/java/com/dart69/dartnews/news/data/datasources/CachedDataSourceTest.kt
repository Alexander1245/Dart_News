package com.dart69.dartnews.news.data.datasources

import com.dart69.dartnews.news.data.Cache
import com.dart69.dartnews.news.data.assertCollectionsEquals
import com.dart69.dartnews.news.data.createStrings
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

internal fun createListsOfStrings(dataCount: Int): List<List<String>> = List(dataCount) {
    createStrings(it + 1)
}

internal class CachedDataSourceTest {
    private lateinit var cachedDataSource: CachedDataSource<Int, String>

    private suspend fun loadAllData(range: IntRange): List<List<String>> =
        range.map { cachedDataSource.searchBy(it)!! }

    @Before
    fun beforeEach() {
        val cache = Cache.Default<Int, List<String>>()
        cachedDataSource = CachedDataSource.Default(cache)
    }

    @Test
    fun testCacheTenListsOfStrings() = runBlocking {
        val dataCount = 10
        val expected = createListsOfStrings(dataCount).onEachIndexed { index, strings ->
            cachedDataSource.save(index, strings)
            assertCollectionsEquals(strings, cachedDataSource.searchBy(index)!!)
        }
        val actual = (0 until 10).map { cachedDataSource.searchBy(it) }
        assertCollectionsEquals(expected, actual)
    }

    @Test
    fun clear() = runBlocking {
        val dataCount = 10
        val clearCount = 3
        createListsOfStrings(dataCount).forEachIndexed { index, strings ->
            cachedDataSource.save(index, strings)
        }
        repeat(clearCount) {
            cachedDataSource.clear(it)
        }
        val expected = createListsOfStrings(dataCount).subList(clearCount, dataCount)
        val actual = loadAllData(clearCount until dataCount)
        assertCollectionsEquals(expected, actual)
    }
}