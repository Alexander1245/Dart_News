package com.dart69.dartnews.news.presentation

import com.dart69.dartnews.news.domain.model.ArticlesType
import com.dart69.dartnews.news.domain.model.Period

interface ByPeriodLoader {
    fun loadByPeriod(period: Period)
}

interface ByTypeLoader {
    fun loadByType(type: ArticlesType)
}

interface ItemSelector {
    fun selectAll()

    fun unselectAll()
}

interface Fetcher {
    fun fetch()

    object None : Fetcher {
        override fun fetch() {}
    }
}