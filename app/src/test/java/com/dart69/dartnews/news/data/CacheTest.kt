package com.dart69.dartnews.news.data

import kotlinx.coroutines.*
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

inline fun <reified T> assertCollectionsEquals(
    collection1: Collection<T>,
    collection2: Collection<T>
) {
    assertArrayEquals(collection1.toTypedArray(), collection2.toTypedArray())
}

internal fun createStrings(number: Int): List<String> = List(number) {
    "string $it"
}

internal class CacheTest {
    private lateinit var cache: Cache.Default<Int, String>

    private suspend fun getAllCachedData(dataCount: Int): List<String> =
        mutableListOf<String>().apply {
            repeat(dataCount) { key ->
                cache.load(key)?.let {
                    this += it
                }
            }
        }

    @Before
    fun beforeEach() {
        cache = Cache.Default()
    }

    @Test
    fun testSavingAndLoadingTenStrings() = runBlocking {
        val dataCount = 10
        val expected = createStrings(dataCount).onEachIndexed { index, string ->
            cache.save(index, string)
            assertEquals(string, cache.load(index))
        }
        val actual = getAllCachedData(dataCount)
        assertCollectionsEquals(expected, actual)
    }

    @Test
    fun testClearThreeStrings() = runBlocking {
        val dataCount = 3
        val strings = createStrings(dataCount).onEachIndexed { index, string ->
            cache.save(index, string)
        }
        repeat(dataCount) { index ->
            cache.clear(index)
            val expected = strings - strings.subList(0, index + 1).toSet()
            val actual = getAllCachedData(dataCount)
            assertCollectionsEquals(expected, actual)
        }
        val actual = getAllCachedData(dataCount)
        val expected = emptyList<String>()
        assertCollectionsEquals(expected, actual)
    }

    @Test
    fun testSaveDataFromSeveralThreads() = runBlocking {
        val dataCount = 100
        val expected = createStrings(dataCount)
        List(dataCount) { key ->
            launch(Dispatchers.Default) {
                cache.save(key, expected[key])
            }
        }.forEach { it.join() }
        assertCollectionsEquals(expected, getAllCachedData(dataCount))
    }

    @Test
    fun testConcurrentClear() = runBlocking {
        val dataCount = 100
        val expected = createStrings(dataCount)
        repeat(dataCount) { index ->
            cache.save(index, expected[index])
        }
        List(dataCount) { key ->
            launch(Dispatchers.Default) {
                cache.clear(key)
            }
        }.forEach { it.join() }
        assertCollectionsEquals(emptyList(), getAllCachedData(dataCount))
    }
}