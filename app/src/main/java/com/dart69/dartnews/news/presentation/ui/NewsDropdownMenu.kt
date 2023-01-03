package com.dart69.dartnews.news.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dart69.dartnews.R
import com.dart69.dartnews.news.other.DividerItem
import com.dart69.dartnews.news.other.MenuItem
import com.dart69.dartnews.news.other.ClickableTextItem

@Composable
fun NewsDropdownMenu(
    modifier: Modifier = Modifier,
    items: List<MenuItem>,
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = modifier
            .wrapContentSize(Alignment.TopStart)
    ) {
        IconButton(
            onClick = { expanded = true },
            colors = IconButtonDefaults.iconButtonColors(
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(id = R.string.more)
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach {
                MenuItem(menuItem = it, onTouchUp = { expanded = false })
            }
        }
    }
}

@Preview
@Composable
fun NewsDropdownMenuPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {

        NewsDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            items = listOf(
                ClickableTextItem("Delete") {},
                ClickableTextItem("Download") {},
                DividerItem,
                ClickableTextItem("Close") {},
            ),
        )
    }
}