package com.dart69.dartnews.news.presentation

import androidx.lifecycle.viewModelScope
import com.dart69.dartnews.main.presentation.BaseViewModel
import com.dart69.dartnews.main.presentation.ScreenState
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.domain.model.AvailableDispatchers
import com.dart69.dartnews.news.domain.model.Period
import com.dart69.dartnews.news.domain.model.Results
import com.dart69.dartnews.news.domain.networking.ConnectionObserver
import com.dart69.dartnews.news.domain.networking.ConnectionState
import com.dart69.dartnews.news.domain.repository.ArticlesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: ArticlesRepository,
    private val dispatchers: AvailableDispatchers,
    private val connectionObserver: ConnectionObserver
) : BaseViewModel<NewsScreenState>() {
    private val screenState = MutableStateFlow<NewsScreenState>(NewsScreenState.Loading)
    private val periods = MutableStateFlow(Period.Day)
    private val isConnectionAvailable = MutableStateFlow(false)

    init {
        viewModelScope.launch(dispatchers.default) {
            connectionObserver.observeConnectionState().collect { connectionState ->
                isConnectionAvailable.emit(connectionState == ConnectionState.Connected)
            }
        }

        viewModelScope.launch(dispatchers.default) {
            isConnectionAvailable.combine(periods) { isAvailable, period ->
                if (isAvailable || repository.hasLocalData(period)) {
                    repository.observe(period).collect { results ->
                        screenState.emit(results.mapToScreenState(screenStateMapper))
                    }
                } else screenState.emit(NewsScreenState.Disconnected)
            }.collect()
        }
    }

    override fun observeScreenState(): StateFlow<NewsScreenState> = screenState.asStateFlow()
}

sealed class NewsScreenState : ScreenState {
    object Disconnected : NewsScreenState()

    object Loading : NewsScreenState()

    data class Error(val throwable: Throwable) : NewsScreenState()

    data class Completed(val articles: List<Article>) : NewsScreenState()
}

fun <T, R : ScreenState> Results<T>.mapToScreenState(mapper: (Results<T>) -> R): R = mapper(this)

private val screenStateMapper = { results: Results<List<Article>> ->
    when (results) {
        is Results.Completed -> NewsScreenState.Completed(results.data)
        is Results.Loading -> NewsScreenState.Loading
        is Results.Error -> NewsScreenState.Error(results.throwable)
    }
}