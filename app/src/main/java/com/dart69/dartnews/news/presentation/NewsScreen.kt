package com.dart69.dartnews.news.presentation

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dart69.dartnews.R
import com.dart69.dartnews.news.domain.model.Article
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
fun NewsScreen(modifier: Modifier = Modifier, items: List<Article> = getDefaultArticles()) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items) { article ->
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
        item {
            Spacer(modifier = Modifier.height(Dimens.SmallPadding))
        }
    }
}

@Preview
@Composable
fun NewsScreenPreview() {
    NewsScreen(modifier = Modifier.fillMaxSize())
}



