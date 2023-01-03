package com.dart69.dartnews.news.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dart69.dartnews.R
import com.dart69.dartnews.destinations.DetailsScreenDestination
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.domain.model.ArticlesType
import com.dart69.dartnews.news.domain.model.Period
import com.dart69.dartnews.news.other.*
import com.dart69.dartnews.news.presentation.ui.*
import com.dart69.dartnews.ui.values.Dimens
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
private fun newsAppBarMenuItems(viewModel: NewsViewModel): List<MenuItem> {
    val initialList = listOf<MenuItem>(TextItem(stringResource(id = R.string.filter_by_periods)))
    val clickableTextItems = Period.values().map {
        ClickableTextItem(stringResource(id = it.stringRes)) {
            viewModel.loadByPeriod(it)
        }
    }
    return initialList + clickableTextItems
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun appDrawerItems(
    viewModel: NewsViewModel,
): List<DrawerItem> {
    val initialList = listOf(DrawerItem.Spacer(modifier = Modifier.height(Dimens.MediumPadding)))
    val navigationItems = ArticlesType.values().map {
        DrawerItem.NavigationItem(
            icon = it.iconRes,
            onClick = {
                viewModel.loadByType(it)
            },
            label = it.stringRes,
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
    return initialList + navigationItems
}

@Composable
fun NewsStates(
    modifier: Modifier = Modifier,
    screenState: NewsScreenState,
    onTryAgainClick: () -> Unit,
    onArticleClick: (Article) -> Unit,
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
                ArticlesColumn(
                    modifier = Modifier.fillMaxSize(),
                    articles = screenState.articles,
                    onItemClick = onArticleClick
                )
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
fun DrawerItems(
    items: List<DrawerItem>,
    drawerState: DrawerState,
    coroutineScope: CoroutineScope,
) {
    var indexOfSelected by rememberSaveable {
        mutableStateOf(items.indexOfFirstClickable())
    }
    items.forEachIndexed { index, drawerItem ->
        when (drawerItem) {
            is DrawerItem.Spacer -> Spacer(modifier = drawerItem.modifier)
            is DrawerItem.Divider -> Divider(modifier = drawerItem.modifier)
            is DrawerItem.NavigationItem -> NavigationDrawerItem(
                modifier = drawerItem.modifier,
                label = { ContentText(text = stringResource(id = drawerItem.label)) },
                icon = {
                    Icon(
                        painter = painterResource(id = drawerItem.icon),
                        contentDescription = stringResource(id = R.string.navigate)
                    )
                },
                onClick = {
                    coroutineScope.launch { drawerState.close() }
                    indexOfSelected = index
                    drawerItem.onClick()
                },
                selected = index == indexOfSelected,
                colors = NavigationDrawerItemDefaults.colors(
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    scope: CoroutineScope,
    drawerState: DrawerState,
    drawerItems: List<DrawerItem>,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                DrawerItems(
                    items = drawerItems,
                    drawerState = drawerState,
                    coroutineScope = scope
                )
            }
        },
        content = content,
    )
}

@Destination(start = true)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun ScaffoldNewsScreen(
    modifier: Modifier = Modifier,
    viewModel: NewsViewModel = hiltViewModel(),
    navigator: DestinationsNavigator,
) {
    val screenState by viewModel.observeScreenState().collectAsStateWithLifecycle()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    NavigationDrawer(
        drawerItems = appDrawerItems(viewModel),
        drawerState = drawerState,
        scope = scope,
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                NewsTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    titleText = stringResource(id = screenState.title),
                    actions = {
                        screenState.onComplete {
                            ContentText(text = stringResource(id = it.period))
                        }
                        NewsDropdownMenu(
                            items = newsAppBarMenuItems(viewModel = viewModel),
                        )
                    },
                    onIconClick = {
                        scope.launch {
                            drawerState.open()
                        }
                    }
                )
            }
        ) {
            NewsStates(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                screenState = screenState,
                onTryAgainClick = viewModel::fetch,
                onArticleClick = { article ->
                    navigator.navigate(direction = DetailsScreenDestination(article))
                }
            )
        }
    }

}

@SuppressLint("ComposableNaming")
@Composable
fun NewsScreenState.onComplete(block: @Composable (NewsScreenState.Completed) -> Unit) {
    if (this is NewsScreenState.Completed) {
        block(this)
    }
}

