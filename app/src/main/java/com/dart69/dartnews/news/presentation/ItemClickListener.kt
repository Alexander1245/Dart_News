package com.dart69.dartnews.news.presentation

import com.dart69.dartnews.news.domain.model.Article

interface ItemClickListener<T> {
    fun onItemClick(item: T)

    fun onItemLongClick(item: T)
}

interface ArticleClickListener: ItemClickListener<Article> {

    object None : ArticleClickListener {
        override fun onItemClick(item: Article) {}

        override fun onItemLongClick(item: Article) {}
    }
}