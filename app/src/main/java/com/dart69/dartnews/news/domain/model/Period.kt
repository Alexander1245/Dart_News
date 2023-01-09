package com.dart69.dartnews.news.domain.model

import androidx.annotation.StringRes
import com.dart69.dartnews.R
import com.dart69.dartnews.news.presentation.Translatable

enum class Period(val value: Int, @StringRes override val stringRes: Int) : Translatable {

    Day(1, R.string.day), Week(7, R.string.week), Month(30, R.string.month)
}