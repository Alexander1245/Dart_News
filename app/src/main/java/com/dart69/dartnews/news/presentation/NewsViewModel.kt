package com.dart69.dartnews.news.presentation

import androidx.annotation.StringRes
import androidx.lifecycle.viewModelScope
import com.dart69.dartnews.R
import com.dart69.dartnews.main.presentation.ScreenState
import com.dart69.dartnews.main.presentation.StatefulViewModel
import com.dart69.dartnews.news.domain.model.*
import com.dart69.dartnews.news.domain.usecase.FetchArticlesUseCase
import com.dart69.dartnews.news.networking.ConnectionRefusedException
import com.dart69.dartnews.news.other.AvailableDispatchers
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
    dispatchers: AvailableDispatchers,
    private val fetchArticlesUseCase: FetchArticlesUseCase,
) : StatefulViewModel<NewsScreenState>(NewsScreenState.Loading(R.string.most_viewed), dispatchers) {
    private val articleDetails = MutableStateFlow(ArticleDetails.Default)

    init {
        fetch()
    }

    fun fetch() {
        viewModelScope.launch(dispatchers.default) {
            fetchArticlesUseCase(articleDetails).collect {
                emitScreenState(it.toScreenState(articleDetails.value))
            }
        }
    }

    fun loadByPeriod(period: Period) {
        articleDetails.update { key ->
            key.copy(period = period)
        }
    }

    fun loadByType(type: ArticlesType) {
        articleDetails.update { key ->
            key.copy(type = type)
        }
    }
}

sealed class NewsScreenState : ScreenState {
    abstract val title: Int

    data class Disconnected(@StringRes override val title: Int) : NewsScreenState()

    data class Loading(@StringRes override val title: Int) : NewsScreenState()

    data class Error(val throwable: Throwable, @StringRes override val title: Int) :
        NewsScreenState()

    data class Completed(
        val articles: List<Article>,
        @StringRes val period: Int,
        @StringRes override val title: Int,
    ) : NewsScreenState()
}

private fun Results<List<Article>>.toScreenState(key: ArticleDetails): NewsScreenState {
    val titleRes = key.type.stringRes
    val periodRes = key.period.stringRes
    val errorMapper = { throwable: Throwable ->
        if (throwable is ConnectionRefusedException) NewsScreenState.Disconnected(titleRes)
        else NewsScreenState.Error(throwable, titleRes)
    }
    return when (this) {
        is Results.Completed -> NewsScreenState.Completed(data, periodRes, titleRes)
        is Results.Loading -> NewsScreenState.Loading(titleRes)
        is Results.Error -> errorMapper(throwable)
    }
}