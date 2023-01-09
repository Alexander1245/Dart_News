package com.dart69.dartnews.news.presentation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.dart69.dartnews.R
import com.dart69.dartnews.news.presentation.ui.ContentText

interface Drawable {
    @Composable
    fun Draw()
}

sealed class DrawerItem : Drawable {

    data class SpacerItem(private val modifier: Modifier) : DrawerItem() {
        @Composable
        override fun Draw() = Spacer(modifier = modifier)
    }

    data class NavigationItem(
        val modifier: Modifier = Modifier,
        @DrawableRes val icon: Int,
        @StringRes val label: Int,
        val onItemClick: () -> Unit,
        val isSelected: Boolean = false,
    ) : DrawerItem() {

        @OptIn(ExperimentalMaterial3Api::class)
        @Composable
        override fun Draw() {
            NavigationDrawerItem(
                modifier = modifier,
                label = { ContentText(text = stringResource(id = label)) },
                icon = {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = stringResource(id = R.string.navigate)
                    )
                },
                onClick = onItemClick,
                selected = isSelected,
                colors = NavigationDrawerItemDefaults.colors(
                    selectedTextColor = MaterialTheme.colorScheme.onSurface,
                    unselectedTextColor = MaterialTheme.colorScheme.onSecondary
                )
            )
        }
    }
}