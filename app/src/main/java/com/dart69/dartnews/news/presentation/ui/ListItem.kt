package com.dart69.dartnews.news.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.dart69.dartnews.R
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.ui.theme.NewsCardDefaults
import com.dart69.dartnews.ui.theme.NewsListItemDefaults
import com.dart69.dartnews.ui.values.Dimens

@Composable
fun DartCard(
    modifier: Modifier = Modifier,
    colors: CardColors = NewsCardDefaults.cardColors(),
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        colors = colors,
    ) {
        content(this)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = NewsListItemDefaults.paddingValues(),
    avatar: @Composable RowScope.() -> Unit,
    title: String,
    content: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    isSelected: Boolean = false,
) {
    Box(modifier = modifier) {
        DartCard(
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    onClick = onClick, onLongClick = onLongClick
                )
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(
                    modifier = Modifier
                        .weight(0.85f)
                        .padding(contentPadding)
                ) {
                    TitleText(
                        modifier = Modifier.fillMaxWidth(),
                        text = title,
                        maxLines = 1
                    )
                    ContentText(
                        modifier = Modifier.fillMaxWidth(),
                        text = content,
                        maxLines = 2,
                        minLines = 2,
                    )
                }
                avatar()
            }
        }
        if (isSelected) {
            Icon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(Dimens.SmallPadding),
                imageVector = Icons.Default.CheckCircle,
                contentDescription = stringResource(id = R.string.selected),
                tint = MaterialTheme.colorScheme.onTertiary,
            )
        }
    }
}

@Composable
fun AsyncAvatar(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String,
    filterQuality: FilterQuality = FilterQuality.High,
    shape: Shape = CircleShape,
    aspectRation: Float = 1.0f
) {
    Box(
        modifier = Modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            modifier = modifier
                .aspectRatio(aspectRation)
                .clip(shape),
            contentScale = ContentScale.Crop,
            model = model,
            contentDescription = contentDescription,
            filterQuality = filterQuality,
            loading = {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            },
            error = {
                Image(
                    painter = painterResource(id = R.drawable.nytimes_logo),
                    contentDescription = contentDescription,
                )
            },
            success = {
                SubcomposeAsyncImageContent()
            }
        )
    }
}

@Preview
@Composable
fun ArticleItemPreview() {
    val article = Article(
        id = 0,
        title = stringResource(id = R.string.test_large_title),
        content = stringResource(id = R.string.test_large_content),
        titleImageUrl = "",
        sourceUrl = "",
        "",
        ""
    )
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.ListItemHeight)
            .padding(
                start = Dimens.SmallPadding,
                end = Dimens.SmallPadding,
                top = Dimens.SmallPadding
            ),
        isSelected = true,
        title = article.title,
        content = article.content,
        onClick = {},
        onLongClick = {},
        avatar = {
            AsyncAvatar(
                modifier = Modifier.padding(
                    top = Dimens.SmallPadding,
                    bottom = Dimens.SmallPadding,
                    end = Dimens.SmallPadding
                ),
                model = R.drawable.image_sample,
                contentDescription = stringResource(id = R.string.article_avatar)
            )
        }
    )
}
