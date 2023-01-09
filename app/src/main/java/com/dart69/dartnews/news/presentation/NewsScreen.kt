package com.dart69.dartnews.news.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dart69.dartnews.destinations.DetailsScreenDestination
import com.dart69.dartnews.details.presentation.collectAsEffectWithLifecycle
import com.dart69.dartnews.news.presentation.ui.*
import com.dart69.dartnews.ui.theme.NewsDrawerDefaults
import com.dart69.dartnews.ui.theme.NewsDropDownDefaults
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    drawerItems: List<DrawerItem>,
    gesturesEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    ModalNavigationDrawer(
        modifier = modifier,
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet {
                drawerItems.forEach { it.Draw() }
            }
        },
        content = content,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticlesTopAppBar(
    modifier: Modifier = Modifier,
    title: Int,
    period: Int,
    isMenuButtonEnabled: Boolean,
    isPeriodsEnabled: Boolean,
    isGroupActionsEnabled: Boolean,
    periodsLoader: ByPeriodLoader,
    selector: ItemSelector,
    onMenuClick: () -> Unit,
) {
    CustomTopAppBar(
        modifier = modifier,
        titleText = stringResource(id = title),
        isIconButtonEnabled = isMenuButtonEnabled,
        actions = {
            NewsDropdownMenu(
                isEnabled = isPeriodsEnabled,
                items = NewsDropDownDefaults.periods(loader = periodsLoader),
                content = {
                    ContentText(text = stringResource(id = period))
                }
            )
            NewsDropdownMenu(
                isEnabled = isGroupActionsEnabled,
                items = NewsDropDownDefaults.groupActions(selector = selector),
                buttonClass = Button.Icon::class
            )
        },
        onIconClick = onMenuClick,
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
    val selectedTab by viewModel.observeSelectedTab().collectAsStateWithLifecycle()
    val selectedPeriod by viewModel.observeSelectedPeriod().collectAsStateWithLifecycle()
    val title by viewModel.observeTitle().collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    val completedState = screenState as? NewsScreenState.Completed
    val isDrawerEnabled = completedState?.isDrawerEnabled ?: true
    val isGroupActionsEnabled = completedState?.isGroupActionsEnabled ?: false
    val isPeriodsEnabled = completedState?.isPeriodsEnabled ?: true

    viewModel.obtainEvent().collectAsEffectWithLifecycle(LocalLifecycleOwner.current) {
        navigator.navigate(direction = DetailsScreenDestination(it.article))
    }

    NavigationDrawer(
        drawerItems = NewsDrawerDefaults.drawerItems(
            loader = viewModel,
            selectedTab = selectedTab,
            coroutineScope = coroutineScope,
            drawerState = drawerState,
        ),
        drawerState = drawerState,
        gesturesEnabled = isDrawerEnabled,
    ) {
        Scaffold(
            modifier = modifier,
            topBar = {
                ArticlesTopAppBar(
                    modifier = Modifier.fillMaxWidth(),
                    title = title,
                    period = selectedPeriod,
                    isMenuButtonEnabled = isDrawerEnabled,
                    isPeriodsEnabled = isPeriodsEnabled,
                    isGroupActionsEnabled = isGroupActionsEnabled,
                    periodsLoader = viewModel,
                    selector = viewModel,
                    onMenuClick = { coroutineScope.launch { drawerState.open() } },
                )
            }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                contentAlignment = Alignment.Center,
            ) {
                screenState.Draw()
            }
        }
    }

}
