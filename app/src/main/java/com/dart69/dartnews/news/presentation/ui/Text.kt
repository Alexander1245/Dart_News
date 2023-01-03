package com.dart69.dartnews.news.presentation.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.dart69.dartnews.ui.theme.Typography

@Composable
fun TitleText(
    modifier: Modifier = Modifier,
    text: String,
    style: TextStyle = Typography.titleLarge,
    fontWeight: FontWeight = FontWeight.Bold,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null,
) {
    Text(
        modifier = modifier,
        text = text,
        style = style,
        fontWeight = fontWeight,
        color = color,
        overflow = overflow,
        minLines = minLines,
        maxLines = maxLines,
        textAlign = textAlign,
    )
}

@Composable
fun ContentText(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.Medium,
    style: TextStyle = Typography.bodyLarge,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null,
) {
    TitleText(
        modifier = modifier,
        text = text,
        color = color,
        fontWeight = fontWeight,
        style = style,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        textAlign = textAlign
    )
}

@Composable
fun SmallText(
    modifier: Modifier = Modifier,
    text: String,
    fontWeight: FontWeight = FontWeight.Medium,
    style: TextStyle = Typography.labelSmall,
    color: Color = MaterialTheme.colorScheme.onPrimary,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign? = null,
) {
    ContentText(
        modifier = modifier,
        text = text,
        fontWeight = fontWeight,
        style = style,
        color = color,
        overflow = overflow,
        minLines = minLines,
        maxLines = maxLines,
        textAlign = textAlign,
    )
}