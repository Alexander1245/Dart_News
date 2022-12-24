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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
fun <T1, T2, R> Flow<T1>.combineFlatten(
    other: Flow<T2>,
    concurrency: Int = DEFAULT_CONCURRENCY,
    transform: suspend (T1, T2) -> Flow<R>,

    ): Flow<R> {
    return this.combine(other) { data1, data2 ->
        transform(data1, data2)
    }.flattenMerge(concurrency)
}

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: ArticlesRepository,
    private val dispatchers: AvailableDispatchers,
    private val connectionObserver: ConnectionObserver
) : BaseViewModel<NewsScreenState>() {
    private val screenState = MutableStateFlow<NewsScreenState>(NewsScreenState.Loading)
    private val period = MutableStateFlow(Period.Day)

    init {
        fetch()
    }

    override fun observeScreenState(): StateFlow<NewsScreenState> = screenState.asStateFlow()

    fun fetch() {
        viewModelScope.launch(dispatchers.default) {
            connectionObserver.observeConnectionState().map { connectionState ->
                connectionState == ConnectionState.Connected
            }.combineFlatten(period) { isConnected, period ->
                if (isConnected || repository.hasLocalData(period)) {
                    repository.observe(period).map { toScreenState(it, period) }
                } else flowOf(NewsScreenState.Disconnected)
            }.collect { state ->
                screenState.emit(state)
            }
        }
    }

    fun changePeriod(newPeriod: Period) {
        viewModelScope.launch(dispatchers.default) {
            period.emit(newPeriod)
        }
    }
}

sealed class NewsScreenState : ScreenState {
    object Disconnected : NewsScreenState()

    object Loading : NewsScreenState()

    data class Error(val throwable: Throwable) : NewsScreenState()

    data class Completed(val articles: List<Article>, val period: Period) : NewsScreenState()
}

private val toScreenState = { results: Results<List<Article>>, period: Period ->
    when (results) {
        is Results.Completed -> NewsScreenState.Completed(results.data, period)
        is Results.Loading -> NewsScreenState.Loading
        is Results.Error -> NewsScreenState.Error(results.throwable)
    }
}