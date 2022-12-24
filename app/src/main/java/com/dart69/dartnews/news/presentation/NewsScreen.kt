package com.dart69.dartnews.news.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.dart69.dartnews.R
import com.dart69.dartnews.news.domain.model.DividerItem
import com.dart69.dartnews.news.domain.model.MenuItem
import com.dart69.dartnews.news.domain.model.Period
import com.dart69.dartnews.news.domain.model.TextMenuItem
import com.dart69.dartnews.ui.values.Dimens

@Composable
private fun newsAppBarMenuItems(viewModel: NewsViewModel): List<MenuItem> = listOf(
    TextMenuItem(stringResource(id = R.string.filter_by_periods)) {},
    DividerItem,
    TextMenuItem(stringResource(id = R.string.today)) {
        viewModel.changePeriod(Period.Day)
    },
    TextMenuItem(stringResource(id = R.string.week)) {
        viewModel.changePeriod(Period.Week)
    },
    TextMenuItem(stringResource(id = R.string.month)) {
        viewModel.changePeriod(Period.Month)
    }
)

@Composable
fun NewsScreen(
    modifier: Modifier = Modifier,
    screenState: NewsScreenState,
    onTryAgainClick: () -> Unit,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (screenState) {
            is NewsScreenState.Loading -> {
                ProgressBox(message = stringResource(id = R.string.loading_please_wait))
            }
            is NewsScreenState.Completed -> {
                ArticlesColumn(modifier = Modifier.fillMaxSize(), articles = screenState.articles)
            }
            is NewsScreenState.Error -> {
                AlertBox(
                    modifier = Modifier.padding(Dimens.SmallPadding),
                    titleText = stringResource(id = R.string.internal_error),
                    description = screenState.throwable.message.orEmpty()
                ) {
                    Button(onClick = onTryAgainClick) {
                        Text(text = stringResource(id = R.string.try_again))
                    }
                }
            }
            is NewsScreenState.Disconnected -> {
                AlertBox(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(Dimens.SmallPadding),
                    titleText = stringResource(id = R.string.disconnected),
                    description = stringResource(id = R.string.what_went_wrong),
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldNewsScreen(
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel(),
) {
    val screenState by viewModel.observeScreenState().collectAsState()
    Scaffold(
        modifier = modifier,
        topBar = {
            NewsTopAppBar(
                modifier = Modifier.fillMaxWidth(),
                actions = {
                    NewsDropdownMenu(
                        items = newsAppBarMenuItems(viewModel = viewModel),
                    )
                }
            )
        }
    ) {
        NewsScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            screenState = screenState,
            onTryAgainClick = viewModel::fetch,
        )
    }
}


