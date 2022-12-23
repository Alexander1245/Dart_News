package com.dart69.dartnews.news.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.dart69.dartnews.R
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.ui.theme.NewsCardDefaults
import com.dart69.dartnews.ui.theme.NewsListItemDefaults
import com.dart69.dartnews.ui.theme.Typography
import com.dart69.dartnews.ui.values.Dimens
import java.util.*

@Composable
fun ListItem(
    modifier: Modifier = Modifier,
    colors: CardColors = NewsCardDefaults.cardColors(),
    contentPadding: PaddingValues = NewsListItemDefaults.paddingValues(),
    avatar: @Composable RowScope.() -> Unit,
    title: String,
    content: String,
) {
    ElevatedCard(modifier = modifier, colors = colors) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                modifier = Modifier
                    .weight(0.85f)
                    .padding(contentPadding)
            ) {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    style = Typography.titleLarge,
                    maxLines = 1,
                    minLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = content,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = Typography.bodyLarge
                )
            }
            avatar()
        }
    }
}

@Composable
fun CircleAsyncAvatar(
    modifier: Modifier = Modifier,
    model: Any?,
    contentDescription: String
) {
    Box(
        modifier = Modifier.wrapContentSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
        AsyncImage(
            modifier = modifier
                .padding(
                    top = Dimens.SmallPadding,
                    bottom = Dimens.SmallPadding,
                    end = Dimens.SmallPadding
                )
                .aspectRatio(1.0f)
                .clip(CircleShape),
            model = model,
            contentDescription = contentDescription,
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview
@Composable
fun ArticleItemPreview() {
    val article = Article(
        title = stringResource(id = R.string.test_large_title),
        content = stringResource(id = R.string.test_large_content),
        titleImageUrl = "",
        publishDate = Date().toString(),
        byLine = stringResource(id = R.string.test_author)
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
        title = article.title,
        content = article.content,
        avatar = {
            CircleAsyncAvatar(
                model = R.drawable.image_sample,
                contentDescription = "Avatar"
            )
        }
    )
}
