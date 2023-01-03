package com.dart69.dartnews.main.presentation

import androidx.lifecycle.ViewModel
import com.dart69.dartnews.news.other.AvailableDispatchers
import kotlinx.coroutines.flow.*

interface ScreenState {
    companion object : ScreenState
}

interface SingleUiEvent {
    companion object : SingleUiEvent
}

interface ScreenObserver<S : ScreenState> {
    fun observeScreenState(): StateFlow<S>
}

interface EventObserver<A : SingleUiEvent> {
    fun obtainEvent(): SharedFlow<A>
}

interface Communication<S : ScreenState, A : SingleUiEvent> : ScreenObserver<S>, EventObserver<A>

abstract class EventViewModel<A : SingleUiEvent>(
    protected val dispatchers: AvailableDispatchers,
) : ViewModel(), EventObserver<A> {
    private val events = MutableSharedFlow<A>()

    protected suspend fun sendEvent(newEvent: A) {
        events.emit(newEvent)
    }

    override fun obtainEvent(): SharedFlow<A> = events.asSharedFlow()
}

abstract class StatefulViewModel<S : ScreenState>(
    initialState: S,
    protected val dispatchers: AvailableDispatchers,
) : ViewModel(), ScreenObserver<S> {
    private val states = MutableStateFlow(initialState)

    protected suspend fun emitScreenState(newState: S) {
        states.emit(newState)
    }

    override fun observeScreenState(): StateFlow<S> = states.asStateFlow()
}

abstract class BaseViewModel<S : ScreenState, A : SingleUiEvent>(
    initialState: S,
    protected val dispatchers: AvailableDispatchers,
) : ViewModel(), Communication<S, A> {
    private val states = MutableStateFlow(initialState)
    private val events = MutableSharedFlow<A>()

    protected suspend fun sendEvent(newEvent: A) {
        events.emit(newEvent)
    }

    protected suspend fun emitState(newState: S) {
        states.emit(newState)
    }

    override fun observeScreenState(): StateFlow<S> = states.asStateFlow()

    override fun obtainEvent(): SharedFlow<A> = events.asSharedFlow()
}