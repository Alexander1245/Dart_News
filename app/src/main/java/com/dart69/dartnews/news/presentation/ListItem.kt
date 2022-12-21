package com.dart69.dartnews.news.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
    avatarShape: Shape = CircleShape,
    contentPadding: PaddingValues = NewsListItemDefaults.paddingValues(),
    title: String,
    content: String,
    avatar: Painter
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
            Image(
                painter = avatar,
                contentDescription = stringResource(id = R.string.article_avatar),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 10.dp, bottom = 10.dp, end = 10.dp)
                    .aspectRatio(1.0f)
                    .clip(avatarShape)
            )
        }
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
        avatar = painterResource(id = R.drawable.image_sample)
    )
}
