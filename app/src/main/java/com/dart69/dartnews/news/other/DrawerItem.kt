package com.dart69.dartnews.news.other

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.Modifier

interface Clickable {
    val onClick: () -> Unit
}

sealed class DrawerItem {
    data class Spacer(val modifier: Modifier = Modifier): DrawerItem()

    data class Divider(val modifier: Modifier = Modifier) : DrawerItem()

    data class NavigationItem(
        val modifier: Modifier = Modifier,
        @DrawableRes val icon: Int,
        @StringRes val label: Int,
        override val onClick: () -> Unit,
    ) : DrawerItem(), Clickable
}

fun Iterable<DrawerItem>.indexOfFirstClickable(): Int = indexOfFirst { it is Clickable }
