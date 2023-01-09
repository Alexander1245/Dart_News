package com.dart69.dartnews.news.selection

import com.dart69.dartnews.news.domain.model.Identifiable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

fun <T> SelectionTracker<T>.toggle(item: T) {
    val action = if(isSelected(item)) this::unselect else this::select
    action(item)
}

interface SelectionTracker<T> {
    fun isSelected(item: T): Boolean

    fun select(item: T)

    fun unselect(item: T)

    fun observeSelected(): StateFlow<Set<Long>>

    fun hasSelection(): Boolean

    class Default<T : Identifiable> : SelectionTracker<T> {
        private val selectedKeys = MutableStateFlow(HashSet<Long>())

        override fun isSelected(item: T): Boolean = item.id in selectedKeys.value

        override fun select(item: T) {
            selectedKeys.update { selected ->
                HashSet(selected + item.id)
            }
        }

        override fun unselect(item: T) {
            selectedKeys.update { selected ->
                HashSet(selected - item.id)
            }
        }

        override fun observeSelected(): StateFlow<Set<Long>> = selectedKeys.asStateFlow()

        override fun hasSelection(): Boolean = selectedKeys.value.isNotEmpty()
    }
}