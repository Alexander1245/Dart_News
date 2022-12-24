package com.dart69.dartnews.news.presentation

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
import com.dart69.dartnews.ui.theme.Typography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsTopAppBar(
    modifier: Modifier = Modifier,
    colors: TopAppBarColors = NewsTopAppBarDefaults.topAppBarColors(),
    titleText: String = stringResource(id = R.string.app_name),
    navigationIcon: ImageVector = Icons.Default.Menu,
    iconContentDescription: String = stringResource(id = R.string.open_menu),
    onIconClick: () -> Unit = {},
    actions:  @Composable (RowScope.() -> Unit) = {}
) {
    TopAppBar(
        modifier = modifier,
        colors = colors,
        title = {
            Text(text = titleText, style = Typography.titleLarge)
        },
        navigationIcon = {
            IconButton(onClick = onIconClick) {
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
    NewsTopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    )
}