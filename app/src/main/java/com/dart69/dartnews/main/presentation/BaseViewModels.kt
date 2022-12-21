package com.dart69.dartnews.main.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

interface ScreenState

interface SingleUiAction

abstract class BaseViewModel<S: ScreenState> : ViewModel() {
    abstract fun observeScreenState(): StateFlow<S>
}

abstract class ActionViewModel<S: ScreenState, A: SingleUiAction>: BaseViewModel<S>() {
    abstract fun obtainEvent(): SharedFlow<A>
}