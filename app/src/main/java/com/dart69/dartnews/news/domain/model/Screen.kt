package com.dart69.dartnews.news.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dart69.dartnews.R

sealed class Screen(
    val route: String,
    @StringRes val labelId: Int,
    @DrawableRes val iconId: Int,
    @StringRes val contentDescriptionId: Int
) {
    object News : Screen("News", R.string.news, R.drawable.ic_article, R.string.to_news_screen)

    companion object {
        fun findAll(): List<Screen> = listOf(
            News,
        )
    }
}
