package com.dart69.dartnews.news.presentation

import androidx.lifecycle.viewModelScope
import com.dart69.dartnews.R
import com.dart69.dartnews.main.presentation.BaseViewModel
import com.dart69.dartnews.main.presentation.SingleUiEvent
import com.dart69.dartnews.news.di.AvailableDispatchers
import com.dart69.dartnews.news.domain.model.*
import com.dart69.dartnews.news.domain.usecase.FetchArticlesUseCase
import com.dart69.dartnews.news.selection.ArticlesSelectionTracker
import com.dart69.dartnews.news.selection.toggle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OpenDetailsScreen(val article: Article) : SingleUiEvent

@HiltViewModel
class NewsViewModel @Inject constructor(
    dispatchers: AvailableDispatchers,
    mapperBuilder: NewsScreenStateMapperBuilder,
    private val fetchArticlesUseCase: FetchArticlesUseCase,
    private val tracker: ArticlesSelectionTracker,
) : BaseViewModel<NewsScreenState, OpenDetailsScreen>(NewsScreenState.Loading, dispatchers),
    ByPeriodLoader, ByTypeLoader, ItemSelector, ArticleClickListener, Fetcher {
    private val articleDetails = MutableStateFlow(ArticleDetails.Default)
    private val articles = MutableStateFlow<Results<List<Article>>>(Results.Loading())
    private val selectedPeriod = articleDetails.map {
        it.period.stringRes
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), R.string.day)
    private val selectedTab = articleDetails.map {
        it.type
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), ArticlesType.MostViewed)
    private val title = selectedTab.map {
        it.stringRes
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), R.string.most_viewed)

    init {
        fetch()

        val mapper = mapperBuilder
            .bindTracker(tracker)
            .bindFetcher(this)
            .bindArticleClickListener(this)
            .build()

        viewModelScope.launch(dispatchers.default) {
            tracker.observeSelected().combine(articles) { keys, results ->
                results.mapResults { list ->
                    list.map { it.copy(isSelected = it.id in keys) }
                }
            }.collect { results ->
                emitState(mapper.map(results))
            }
        }
    }

    fun observeSelectedTab(): StateFlow<ArticlesType> = selectedTab

    fun observeSelectedPeriod(): StateFlow<Int> = selectedPeriod

    fun observeTitle(): StateFlow<Int> = title

    override fun fetch() {
        viewModelScope.launch(dispatchers.default) {
            fetchArticlesUseCase(articleDetails).collect {
                articles.emit(it)
            }
        }
    }

    override fun onItemClick(item: Article) {
        if (tracker.hasSelection()) {
            tracker.toggle(item)
        } else {
            viewModelScope.launch(dispatchers.default) {
                sendEvent(OpenDetailsScreen(item))
            }
        }
    }

    override fun onItemLongClick(item: Article) {
        tracker.toggle(item)
    }

    override fun unselectAll() {
        val items = articles.value.takeCompleted() ?: return
        items.forEach(tracker::unselect)
    }

    override fun selectAll() {
        val items = articles.value.takeCompleted() ?: return
        items.forEach(tracker::select)
    }

    override fun loadByPeriod(period: Period) {
        articleDetails.update { key ->
            key.copy(period = period)
        }
    }

    override fun loadByType(type: ArticlesType) {
        articleDetails.update { key ->
            key.copy(type = type)
        }
    }
}