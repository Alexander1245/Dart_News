package com.dart69.dartnews.news.selection

import com.dart69.dartnews.news.domain.model.Article
import javax.inject.Inject

private typealias BaseTracker = SelectionTracker<Article>
private typealias TrackerImplementation = SelectionTracker.Default<Article>

interface ArticlesSelectionTracker : SelectionTracker<Article> {

    class Default @Inject constructor() : ArticlesSelectionTracker,
        BaseTracker by TrackerImplementation()

    object None : ArticlesSelectionTracker {
        override fun isSelected(item: Article): Boolean = false

        override fun select(item: Article) {}

        override fun unselect(item: Article) {}

        override fun observeSelected(): Nothing = error("Illegal operation in None tracker.")

        override fun hasSelection(): Boolean = false
    }
}