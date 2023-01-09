package com.dart69.dartnews.news.selection

import com.dart69.dartnews.news.data.assertCollectionsEquals
import com.dart69.dartnews.news.domain.model.Identifiable
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

data class SelectableTestModel(override val id: Long, val string: String) : Identifiable

internal class SelectionTrackerTest {
    private lateinit var tracker: SelectionTracker.Default<SelectableTestModel>
    private lateinit var testItems: List<SelectableTestModel>

    @Before
    fun beforeEach() {
        tracker = SelectionTracker.Default()
        testItems = List(10) {
            SelectableTestModel(it.toLong(), "String $it")
        }
    }

    @Test
    fun selectItems() {
        testItems.forEach(tracker::select)
        testItems.forEach { assertTrue(tracker.isSelected(it)) }
    }

    @Test
    fun selectDifferentItemsWithSameKeys() {
        testItems.forEach(tracker::select)
        val newItems = testItems.map { it.copy(string = "aaa ${it.id}") }
        testItems.forEach { assertTrue(tracker.isSelected(it)) }
        newItems.forEach { assertTrue(tracker.isSelected(it)) }
    }

    @Test
    fun unselect() {
        testItems.forEach(tracker::select)
        testItems
            .take(3)
            .onEach { tracker.unselect(it) }
            .onEach { assertFalse(tracker.isSelected(it)) }

        testItems
            .drop(3)
            .forEach { assertTrue(tracker.isSelected(it)) }
    }

    @Test
    fun observeSelected() = runBlocking {
        val items = mutableListOf<SelectableTestModel>()
        launch {
            withTimeout(1500) {
                tracker.observeSelected().drop(1).collect {
                    assertCollectionsEquals(items, it)
                }
            }
        }
        items.addAll(List(3) {
            SelectableTestModel(it.toLong(), "String $it")
        })
        items.forEach { tracker.select(it) }
    }
}