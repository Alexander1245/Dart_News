package com.dart69.dartnews.news.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.dart69.dartnews.R
import com.dart69.dartnews.ui.theme.Typography
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
        Text(
            text = titleText,
            style = Typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = description,
            style = Typography.bodyLarge
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
                Text(text = stringResource(id = R.string.try_again))
            }
        }
    }
}