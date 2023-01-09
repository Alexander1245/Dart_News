package com.dart69.dartnews.ui.theme

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.dart69.dartnews.R
import com.dart69.dartnews.news.domain.model.ArticlesType
import com.dart69.dartnews.news.domain.model.Period
import com.dart69.dartnews.news.presentation.*
import com.dart69.dartnews.ui.values.Dimens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
object NewsTopAppBarDefaults {

    @Composable
    fun topAppBarColors(
        containerColor: Color = MaterialTheme.colorScheme.primary,
        titleContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        navigationIconColor: Color = MaterialTheme.colorScheme.onPrimary,
        actionIconContentColor: Color = MaterialTheme.colorScheme.onPrimary
    ): TopAppBarColors = TopAppBarDefaults.topAppBarColors(
        containerColor = containerColor,
        titleContentColor = titleContentColor,
        navigationIconContentColor = navigationIconColor,
        actionIconContentColor = actionIconContentColor
    )
}

object NewsCardDefaults {

    @Composable
    fun cardColors(
        containerColor: Color = MaterialTheme.colorScheme.secondary,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary
    ): CardColors =
        CardDefaults.cardColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
}

object NewsListItemDefaults {

    fun paddingValues(): PaddingValues = PaddingValues(
        start = Dimens.MediumPadding,
        end = Dimens.SmallPadding,
        top = Dimens.MediumPadding,
        bottom = Dimens.SmallPadding
    )
}

object NewsDrawerDefaults {

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun drawerItems(
        loader: ByTypeLoader,
        types: List<ArticlesType> = ArticlesType.values().toList(),
        selectedTab: ArticlesType = types.first(),
        coroutineScope: CoroutineScope,
        drawerState: DrawerState,
    ): List<DrawerItem> =
        mutableListOf<DrawerItem>(DrawerItem.SpacerItem(modifier = Modifier.height(Dimens.MediumPadding))).apply {
            addAll(types.map { type ->
                DrawerItem.NavigationItem(
                    icon = type.iconRes,
                    onItemClick = {
                        coroutineScope.launch { drawerState.close() }
                        loader.loadByType(type)
                    },
                    label = type.stringRes,
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
                    isSelected = type == selectedTab,
                )
            })
        }
}

object NewsDropDownDefaults {

    @Composable
    fun periods(
        loader: ByPeriodLoader,
        periods: List<Period> = Period.values().toList()
    ): List<MenuItem> =
        mutableListOf(
            TextItem(stringResource(id = R.string.filter_by_periods)),
            DividerItem
        ).apply {
            addAll(periods.map {
                ClickableTextItem(stringResource(id = it.stringRes)) { loader.loadByPeriod(it) }
            })
        }

    @Composable
    fun groupActions(selector: ItemSelector): List<MenuItem> =
        listOf(
            TextItem(stringResource(id = R.string.group_actions)),
            DividerItem,
            ClickableTextItem(stringResource(id = R.string.select_all)) {
                selector.selectAll()
            },
            ClickableTextItem(stringResource(id = R.string.unselect_all)) {
                selector.unselectAll()
            },
        )
}