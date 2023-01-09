package com.dart69.dartnews.news.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.dart69.dartnews.news.presentation.ClickableTextItem
import com.dart69.dartnews.news.presentation.DividerItem
import com.dart69.dartnews.news.presentation.MenuItem
import com.dart69.dartnews.news.presentation.TextItem
import com.dart69.dartnews.ui.values.Dimens

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    menuItem: MenuItem,
    onTouchUp: () -> Unit = {}
) {
    when (menuItem) {
        is DividerItem -> Divider(modifier = modifier)
        is TextItem -> Text(
            modifier = Modifier.padding(Dimens.SmallPadding),
            text = menuItem.text,
            fontWeight = FontWeight.Bold
        )
        is ClickableTextItem -> DropdownMenuItem(
            modifier = modifier,
            onClick = {
                menuItem.onClick()
                onTouchUp()
            },
            text = {
                Text(text = menuItem.text)
            }
        )
    }
}

