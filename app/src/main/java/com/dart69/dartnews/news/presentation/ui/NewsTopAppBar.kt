package com.dart69.dartnews.news.presentation.ui

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dart69.dartnews.R
import com.dart69.dartnews.ui.theme.NewsTopAppBarDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = NewsTopAppBarDefaults.topAppBarColors(),
    titleText: String = stringResource(id = R.string.app_name),
    navigationIcon: ImageVector = Icons.Default.Menu,
    iconContentDescription: String = stringResource(id = R.string.open_menu),
    isIconButtonEnabled: Boolean = true,
    onIconClick: () -> Unit = {},
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        modifier = modifier,
        colors = colors,
        title = {
            TitleText(text = titleText)
        },
        navigationIcon = {
            IconButton(onClick = onIconClick, enabled = isIconButtonEnabled) {
                Icon(imageVector = navigationIcon, contentDescription = iconContentDescription)
            }
        },
        actions = actions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NewsTopAppBarPreview() {
    CustomTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}