package com.dart69.dartnews.news.presentation

import com.dart69.dartnews.main.presentation.BaseViewModel
import com.dart69.dartnews.main.presentation.ScreenState
import kotlinx.coroutines.flow.StateFlow

class NewsViewModel : BaseViewModel<NewsScreenState>() {
    override fun observeScreenState(): StateFlow<NewsScreenState> = TODO()
}

sealed class NewsScreenState : ScreenState {
    object ConnectionRefused : NewsScreenState()

    object Loading : NewsScreenState()

    object Error : NewsScreenState()

    object Completed : NewsScreenState()
}