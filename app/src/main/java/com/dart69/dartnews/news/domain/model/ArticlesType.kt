package com.dart69.dartnews.news.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.dart69.dartnews.R
import com.dart69.dartnews.news.presentation.Translatable

interface Iconable {
    val iconRes: Int
}

enum class ArticlesType(
    @StringRes override val stringRes: Int,
    @DrawableRes override val iconRes: Int
) : Translatable, Iconable {
    MostViewed(R.string.most_viewed, R.drawable.ic_eye),
    MostShared(R.string.most_shared, R.drawable.ic_share),
    MostEmailed(R.string.most_emailed, R.drawable.ic_email)
}