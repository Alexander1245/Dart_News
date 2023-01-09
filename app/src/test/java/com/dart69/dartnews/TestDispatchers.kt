package com.dart69.dartnews

import com.dart69.dartnews.news.di.AvailableDispatchers
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher

@OptIn(ExperimentalCoroutinesApi::class)
internal class TestDispatchers : AvailableDispatchers {
    override val io: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val default: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
    override val main: CoroutineDispatcher
        get() = UnconfinedTestDispatcher()
}