package com.dart69.dartnews.news.presentation

import com.dart69.dartnews.main.presentation.ScreenState
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.domain.model.Results
import com.dart69.dartnews.news.domain.model.isConnectionError
import com.dart69.dartnews.news.selection.ArticlesSelectionTracker
import javax.inject.Inject

fun interface ScreenStateMapper<I, O : ScreenState> {
    fun map(results: Results<I>): O
}

interface NewsScreenStateMapperBuilder {
    fun bindTracker(tracker: ArticlesSelectionTracker): NewsScreenStateMapperBuilder

    fun bindArticleClickListener(articleClickListener: ArticleClickListener): NewsScreenStateMapperBuilder

    fun bindFetcher(fetcher: Fetcher): NewsScreenStateMapperBuilder

    fun build(): NewsScreenStateMapper

    class Default @Inject constructor() : NewsScreenStateMapperBuilder {
        private var tracker: ArticlesSelectionTracker = ArticlesSelectionTracker.None
        private var articleClickListener: ArticleClickListener = ArticleClickListener.None
        private var fetcher: Fetcher = Fetcher.None

        override fun bindTracker(tracker: ArticlesSelectionTracker) = apply {
            this.tracker = tracker
        }

        override fun bindArticleClickListener(articleClickListener: ArticleClickListener) = apply {
            this.articleClickListener = articleClickListener
        }

        override fun bindFetcher(fetcher: Fetcher) = apply {
            this.fetcher = fetcher
        }

        override fun build(): NewsScreenStateMapper = NewsScreenStateMapper.Default(
            tracker, articleClickListener, fetcher
        )
    }
}

fun interface NewsScreenStateMapper : ScreenStateMapper<List<Article>, NewsScreenState> {

    class Default(
        private val tracker: ArticlesSelectionTracker,
        private val articleClickListener: ArticleClickListener,
        private val fetcher: Fetcher,
    ) : NewsScreenStateMapper {
        private fun mapErrors(error: Results.Error<List<Article>>): NewsScreenState =
            if (error.isConnectionError()) NewsScreenState.Disconnected else NewsScreenState.Error(
                error.throwable, fetcher::fetch
            )

        private fun mapCompleted(results: Results.Completed<List<Article>>): NewsScreenState.Completed {
            val isInSelectionMode = tracker.hasSelection()
            return NewsScreenState.Completed(
                articles = results.data,
                isDrawerEnabled = !isInSelectionMode,
                isGroupActionsEnabled = isInSelectionMode,
                isPeriodsEnabled = !isInSelectionMode,
                onArticleClick = articleClickListener::onItemClick,
                onArticleLongClick = articleClickListener::onItemLongClick,
            )
        }

        override fun map(results: Results<List<Article>>): NewsScreenState {
            return when (results) {
                is Results.Completed -> mapCompleted(results)
                is Results.Loading -> NewsScreenState.Loading
                is Results.Error -> mapErrors(results)
            }
        }
    }
}