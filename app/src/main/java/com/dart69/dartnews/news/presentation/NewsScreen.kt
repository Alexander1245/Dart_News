package com.dart69.dartnews.news.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.dart69.dartnews.R
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.ui.theme.Typography
import com.dart69.dartnews.ui.values.Dimens
import java.util.*

@Composable
fun getDefaultArticles(size: Int = 30): List<Article> = List(size) {
    Article(
        title = stringResource(id = R.string.test_large_title),
        content = stringResource(id = R.string.test_large_content),
        byLine = stringResource(id = R.string.test_author),
        publishDate = Date().toString(),
        titleImageUrl = "",
    )
}

@Composable
fun NewsScreen(modifier: Modifier = Modifier, viewModel: NewsViewModel = hiltViewModel()) {
    val screenState by viewModel.observeScreenState().collectAsState()
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        when (screenState) {
            is NewsScreenState.Loading -> {
                CircularProgressIndicator()
            }
            is NewsScreenState.Completed -> {
                val items = (screenState as NewsScreenState.Completed).articles
                ArticlesColumn(modifier = Modifier.fillMaxSize(), articles = items)
            }
            is NewsScreenState.Error -> {
                val throwable = (screenState as NewsScreenState.Error).throwable
                Text(
                    text = throwable.message.orEmpty(),
                    style = Typography.titleLarge,
                )
            }
            is NewsScreenState.Disconnected -> {
                Column(modifier = Modifier
                    .wrapContentSize()
                    .padding(Dimens.SmallPadding)
                ) {
                    Text(
                        text = stringResource(id = R.string.disconnected),
                        style = Typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(id = R.string.what_went_wrong),
                        style = Typography.bodyLarge
                    )
                }
            }
        }
    }
}

@Composable
fun ArticlesColumn(modifier: Modifier = Modifier, articles: List<Article>) {
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
                avatar = {
                    CircleAsyncAvatar(
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
fun CircularProgressBarPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Preview
@Composable
fun NewsScreenPreview() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ArticlesColumn(
            modifier = Modifier.fillMaxSize(),
            articles = getDefaultArticles()
        )
    }
}



