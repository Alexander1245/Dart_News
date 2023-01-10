package com.dart69.dartnews.news.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.dart69.dartnews.R
import com.dart69.dartnews.main.presentation.ScreenState
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.presentation.ui.AlertBox
import com.dart69.dartnews.news.presentation.ui.ArticlesColumn
import com.dart69.dartnews.news.presentation.ui.ProgressBox
import com.dart69.dartnews.ui.values.Dimens

sealed class NewsScreenState : ScreenState, Drawable {

    object Disconnected : NewsScreenState() {
        @Composable
        override fun Draw() {
            AlertBox(
                modifier = Modifier
                    .wrapContentSize()
                    .padding(Dimens.SmallPadding),
                titleText = stringResource(id = R.string.disconnected),
                description = stringResource(id = R.string.what_went_wrong),
            )
        }
    }

    object Loading : NewsScreenState() {

        @Composable
        override fun Draw() {
            ProgressBox(message = stringResource(id = R.string.loading_please_wait))
        }
    }

    data class Error(
        private val throwable: Throwable,
        private val onErrorOccurred: () -> Unit
    ) : NewsScreenState() {

        @Composable
        override fun Draw() {
            AlertBox(
                modifier = Modifier.padding(Dimens.SmallPadding),
                titleText = stringResource(id = R.string.internal_error),
                description = throwable.message.orEmpty()
            ) {
                Button(onClick = onErrorOccurred) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }

    data class Completed(
        val isDrawerEnabled: Boolean,
        val isPeriodsEnabled: Boolean,
        val isGroupActionsEnabled: Boolean,
        val articles: List<Article>,
        private val onArticleClick: (Article) -> Unit,
        private val onArticleLongClick: (Article) -> Unit,
    ) : NewsScreenState() {

        @Composable
        override fun Draw() {
            ArticlesColumn(
                modifier = Modifier.fillMaxSize(),
                articles = articles,
                onItemClick = onArticleClick,
                onItemLongClick = onArticleLongClick,
            )
        }
    }
}
