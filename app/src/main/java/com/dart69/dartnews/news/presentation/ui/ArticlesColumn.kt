package com.dart69.dartnews.news.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dart69.dartnews.R
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.ui.values.Dimens

@Composable
internal fun getDefaultArticles(size: Int = 30): List<Article> = List(size) {
    Article(
        id = 0,
        title = stringResource(id = R.string.test_large_title),
        content = stringResource(id = R.string.test_large_content),
        titleImageUrl = "www.testHost.com",
        sourceUrl = "www.testSourceHost.com",
        byLine = "by Dart69",
        publishedDate = "22.02.2022"
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ArticlesColumn(
    modifier: Modifier = Modifier,
    articles: List<Article>,
    onItemClick: (Article) -> Unit,
    onItemLongClick: (Article) -> Unit,
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(articles) { article ->
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
                isSelected = article.isSelected,
                onClick = { onItemClick(article) },
                onLongClick = { onItemLongClick(article) },
                avatar = {
                    AsyncAvatar(
                        modifier = Modifier.padding(
                            top = Dimens.SmallPadding,
                            bottom = Dimens.SmallPadding,
                            end = Dimens.SmallPadding
                        ),
                        model = article.titleImageUrl,
                        contentDescription = stringResource(id = R.string.article_avatar)
                    )
                }
            )
        }
        item {
            Spacer(modifier = Modifier.height(Dimens.SmallPadding))
        }
    }
}

@Preview
@Composable
fun ArticlesColumnPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ArticlesColumn(
            modifier = Modifier.fillMaxSize(),
            articles = getDefaultArticles(),
            onItemClick = {},
            onItemLongClick = {}
        )
    }
}