package com.dart69.dartnews.news.presentation

import com.dart69.dartnews.TestDispatchers
import com.dart69.dartnews.news.domain.model.*
import com.dart69.dartnews.news.domain.usecase.FetchArticlesUseCase
import com.dart69.dartnews.news.selection.ArticlesSelectionTracker
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.util.*

internal class FakeFetchArticlesUseCase(
    private val dataCount: Int = 10,
) : FetchArticlesUseCase {
    private fun createArticle(id: Long, details: ArticleDetails): Article =
        Article(
            id = id,
            title = "${details.type.name}, $id",
            content = "${details.period.name}, $id",
            titleImageUrl = "imageUrl",
            sourceUrl = "sourceUrl",
            byLine = "by fake",
            publishedDate = "11.09.2000",
            isSelected = false
        )

    suspend fun createArticles(details: ArticleDetails): List<Article> =
        List(dataCount) {
            delay(10)
            createArticle(it.toLong(), details)
        }

    @OptIn(FlowPreview::class)
    override fun invoke(details: Flow<ArticleDetails>): ResultsFlow<List<Article>> =
        details.flatMapConcat { articleDetails ->
            resultsFlowOf {
                createArticles(articleDetails)
            }
        }
}

internal class NewsViewModelTest {
    private lateinit var viewModel: NewsViewModel
    private val fetchArticlesUseCase = FakeFetchArticlesUseCase()
    private val timeOut = 333L

    @Before
    fun beforeEach() {
        val dispatchers = TestDispatchers()
        val mapperBuilder = NewsScreenStateMapperBuilder.Default()
        val tracker = ArticlesSelectionTracker.Default()
        viewModel = NewsViewModel(dispatchers, mapperBuilder, fetchArticlesUseCase, tracker)
    }

    @Test
    fun `observe tab emits selected navigation tab`() = runBlocking {
        val expectedSelectedTabs = listOf(
            ArticlesType.MostViewed,
            ArticlesType.MostShared,
            ArticlesType.MostEmailed
        )
        launch {
            withTimeout(timeOut) {
                var index = 0
                viewModel.observeSelectedTab().take(3).collect {
                    assertEquals(expectedSelectedTabs[index++], it)
                }
            }
        }
        viewModel.loadByType(ArticlesType.MostShared)
        viewModel.loadByType(ArticlesType.MostEmailed)
    }

    @Test
    fun `observe period emits expected period string resource`() = runBlocking {
        val expectedPeriods = listOf(
            Period.Day.stringRes,
            Period.Month.stringRes,
            Period.Week.stringRes,
        )
        launch {
            withTimeout(timeOut) {
                var index = 0
                viewModel.observeSelectedPeriod().take(3).collect {
                    assertEquals(expectedPeriods[index++], it)
                }
            }
        }
        viewModel.loadByPeriod(Period.Month)
        viewModel.loadByPeriod(Period.Week)
    }

    @Test
    fun `observe title emits expected title string resources`() = runBlocking {
        val expectedTitles = listOf(
            ArticlesType.MostViewed.stringRes,
            ArticlesType.MostShared.stringRes,
            ArticlesType.MostEmailed.stringRes,
        )
        launch {
            withTimeout(timeOut) {
                var index = 0
                viewModel.observeTitle().take(3).collect {
                    assertEquals(expectedTitles[index++], it)
                }
            }
        }
        viewModel.loadByType(ArticlesType.MostShared)
        viewModel.loadByType(ArticlesType.MostEmailed)
    }

    @Test
    fun `fetch emit loading and completed states`() = runBlocking {
        val expectedScreenStates = listOf(
            NewsScreenState.Loading::class,
            NewsScreenState.Completed::class,
        )
        launch {
            withTimeout(timeOut) {
                var index = 0
                viewModel.observeScreenState().take(2).collect {
                    assertEquals(expectedScreenStates[index++], it::class)
                }
            }
        }
        viewModel.fetch()
    }

    @Test
    fun `click on article without selection mode emit OpenDetailsScreen event`() = runBlocking {
        val currentArticles = fetchArticlesUseCase.createArticles(ArticleDetails.Default)
        val clickedIndexes = listOf(0, 5, 3)
        val events: Queue<OpenDetailsScreen> = ArrayDeque()
        launch {
            withTimeout(timeOut) {
                viewModel.obtainEvent().collect {
                    val expected = events.poll()
                    val article = expected?.article
                    assertEquals(events.poll(), it)
                    assertEquals(article, it.article)
                }
            }
        }
        clickedIndexes.forEach { viewModel.onItemClick(currentArticles[it]) }
    }

    @Test
    fun `click on article in selection mode select them and not obtain events`() = runBlocking {
        val events = mutableListOf<OpenDetailsScreen>()
        val currentArticles = fetchArticlesUseCase.createArticles(ArticleDetails.Default)
        val selectedItems: Queue<Article> = ArrayDeque(currentArticles.take(3))
        viewModel.onItemLongClick(currentArticles[3])
        launch {
            withTimeout(timeOut) {
                viewModel.observeScreenState().filter { it is NewsScreenState.Completed }.collect {
                    assertEquals(selectedItems.poll(), it)
                    assertTrue(events.isEmpty())
                }
            }
        }
        launch {
            withTimeout(timeOut) {
                viewModel.obtainEvent().collect {
                    events += it
                }
            }
        }
        selectedItems.forEach { viewModel.onItemClick(it) }
    }

    @Test
    fun `long click toggle articles selected`() = runBlocking {
        val lastSelectedIndex = 3
        val currentArticles = fetchArticlesUseCase.createArticles(ArticleDetails.Default)
        val selected: Queue<Article> = ArrayDeque(currentArticles.take(lastSelectedIndex))
        launch {
            withTimeout(timeOut) {
                viewModel.observeScreenState().filter {
                    it is NewsScreenState.Completed
                }.collect {
                    val articles = (it as NewsScreenState.Completed).articles
                    articles.forEachIndexed { index, item ->
                        assertEquals(index < lastSelectedIndex, item.isSelected)
                    }
                }
            }
        }
        repeat(selected.size) { viewModel.onItemLongClick(selected.poll()!!) }
    }

    @Test
    fun `long click toggle articles unselected`() = runBlocking {
        var isFirstItemSelected = false
        val items = fetchArticlesUseCase.createArticles(ArticleDetails.Default)
        launch {
            withTimeout(timeOut) {
                viewModel.observeScreenState().filter { it is NewsScreenState.Completed }.collect {
                    val state = it as NewsScreenState.Completed
                    assertEquals(isFirstItemSelected, state.articles.first().isSelected)
                }
            }
        }
        isFirstItemSelected = true
        viewModel.onItemLongClick(items.first())
        isFirstItemSelected = false
        viewModel.onItemLongClick(items.first())
    }

    @Test
    fun unselectAll() = runBlocking {
        selectAll()
        launch {
            withTimeout(timeOut) {
                viewModel.observeScreenState().drop(1)
                    .collect {
                        val articles = (it as NewsScreenState.Completed).articles
                        assertTrue(articles.all { article -> !article.isSelected })
                    }
            }
        }
        viewModel.unselectAll()
    }

    @Test
    fun selectAll() = runBlocking {
        launch {
            withTimeout(timeOut) {
                viewModel.observeScreenState()
                    .filter { it is NewsScreenState.Completed }.drop(1)
                    .collect {
                        val articles = (it as NewsScreenState.Completed).articles
                        assertTrue(articles.all { article -> article.isSelected })
                    }
            }
        }
        viewModel.selectAll()
    }
}