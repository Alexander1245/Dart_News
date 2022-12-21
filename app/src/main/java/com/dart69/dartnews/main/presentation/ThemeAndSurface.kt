package com.dart69.dartnews.main.presentation

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dart69.dartnews.ui.theme.DartNewsTheme

@Composable
fun ThemeAndSurface(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.background,
    content: @Composable () -> Unit
) {
    DartNewsTheme {
        Surface(modifier = modifier, color = color, content = content)
    }
}