package com.dart69.dartnews.news.presentation.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dart69.dartnews.R
import com.dart69.dartnews.ui.values.Dimens

@Composable
fun AlertBox(
    modifier: Modifier = Modifier,
    titleText: String,
    description: String,
    content: @Composable ColumnScope.() -> Unit = {},
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TitleText(
            text = titleText,
            color = MaterialTheme.colorScheme.onSurface
        )
        ContentText(
            text = description,
            color = MaterialTheme.colorScheme.onSurface
        )
        content()
    }
}

@Preview
@Composable
fun AlertBoxPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(Dimens.SmallPadding),
        contentAlignment = Alignment.Center
    ) {
        AlertBox(
            titleText = stringResource(id = R.string.internal_error),
            description = stringResource(id = R.string.what_went_wrong),
        ) {
            Button(
                onClick = { },
            ) {
                ContentText(text = stringResource(id = R.string.try_again))
            }
        }
    }
}