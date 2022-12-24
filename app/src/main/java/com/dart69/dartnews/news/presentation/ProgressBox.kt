package com.dart69.dartnews.news.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dart69.dartnews.R
import com.dart69.dartnews.ui.theme.Typography
import com.dart69.dartnews.ui.values.Dimens

@Composable
fun ProgressBox(
    modifier: Modifier = Modifier,
    message: String,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(Dimens.SmallPadding))
        Text(text = message, style = Typography.bodyLarge)
    }
}

@Preview
@Composable
fun ProgressBoxPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ProgressBox(message = stringResource(id = R.string.loading_please_wait))
    }
}