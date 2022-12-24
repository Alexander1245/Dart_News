package com.dart69.dartnews.news.presentation

import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dart69.dartnews.news.domain.model.DividerItem
import com.dart69.dartnews.news.domain.model.MenuItem
import com.dart69.dartnews.news.domain.model.TextMenuItem

@Composable
fun MenuItem(modifier: Modifier = Modifier, menuItem: MenuItem) {
    when (menuItem) {
        is DividerItem -> Divider(modifier = modifier)
        is TextMenuItem -> DropdownMenuItem(
            modifier = modifier,
            onClick = menuItem.onClick,
            text = {
                Text(text = menuItem.text)
            }
        )
    }
}