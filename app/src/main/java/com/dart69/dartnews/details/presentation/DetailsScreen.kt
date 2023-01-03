package com.dart69.dartnews.details.presentation

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import com.dart69.dartnews.R
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.presentation.ui.*
import com.dart69.dartnews.ui.theme.NewsListItemDefaults
import com.dart69.dartnews.ui.values.Dimens
import com.ramcosta.composedestinations.annotation.Destination

@Composable
fun DetailsAvatarBox(
    modifier: Modifier = Modifier,
    imageUrl: String,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(modifier = modifier) {
        AsyncAvatar(
            modifier = modifier.drawWithCache {
                val gradient = Brush.verticalGradient(
                    colors = listOf(Color.Transparent, Color.Black),
                    startY = size.height / 3,
                    endY = size.height
                )
                onDrawWithContent {
                    drawContent()
                    drawRect(gradient, blendMode = BlendMode.Multiply)
                }
            },
            model = imageUrl,
            contentDescription = stringResource(id = R.string.article_avatar),
            shape = RectangleShape,
        )
        content()
    }
}

@Composable
fun DetailsCard(modifier: Modifier = Modifier, article: Article) {
    DartCard(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth()) {
            DetailsAvatarBox(imageUrl = article.titleImageUrl) {
                ContentText(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(all = Dimens.SmallPadding), text = article.byLine,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(NewsListItemDefaults.paddingValues())
        ) {
            TitleText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = article.title
            )
            ContentText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = article.content
            )
            ContentText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.published, article.publishedDate)
            )
        }
    }
}

@Composable
fun ContentTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    textAlign: TextAlign? = TextAlign.Center
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = onClick
    ) {
        ContentText(
            modifier = Modifier.fillMaxWidth(),
            text = text,
            textAlign = textAlign
        )
    }
}

@Destination
@Composable
fun DetailsScreen(
    modifier: Modifier = Modifier,
    article: Article,
    viewModel: DetailsViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    viewModel.obtainEvent().collectAsEffectWithLifecycle(lifecycleOwner) { event ->
        context.startActivity(Intent(Intent.ACTION_VIEW, event.url.toUri()))
    }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(Dimens.SmallPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            DetailsCard(modifier = Modifier.fillMaxSize(), article = article)
        }
        item {
            Spacer(modifier = Modifier.height(Dimens.SmallPadding))
        }
        item {
            ContentTextButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(id = R.string.open_in_browser),
                onClick = { viewModel.openFullArticleDetails(article) }
            )
        }
    }
}

@Preview
@Composable
fun DetailsScreenPreview() {
    DetailsScreen(
        modifier = Modifier.fillMaxSize(),
        article = getDefaultArticles().first().let {
            val content = it.content.repeat(3)
            it.copy(content = content)
        }
    )
}