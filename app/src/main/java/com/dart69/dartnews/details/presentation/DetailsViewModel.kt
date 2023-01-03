package com.dart69.dartnews.details.presentation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import com.dart69.dartnews.main.presentation.EventViewModel
import com.dart69.dartnews.main.presentation.SingleUiEvent
import com.dart69.dartnews.news.domain.model.Article
import com.dart69.dartnews.news.other.AvailableDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OpenUrlPage(val url: String) : SingleUiEvent

@SuppressLint("ComposableNaming")
@Composable
fun <T> SharedFlow<T>.collectAsEffectWithLifecycle(
    lifecycleOwner: LifecycleOwner,
    lifecycleState: Lifecycle.State = Lifecycle.State.STARTED,
    block: suspend (T) -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        lifecycleOwner.repeatOnLifecycle(lifecycleState) {
            collect(block)
        }
    }
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    dispatchers: AvailableDispatchers,
) : EventViewModel<OpenUrlPage>(dispatchers) {

    fun openFullArticleDetails(article: Article) {
        viewModelScope.launch(dispatchers.default) {
            sendEvent(OpenUrlPage(article.sourceUrl))
        }
    }
}