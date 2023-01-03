package com.dart69.dartnews.news.other

import androidx.annotation.DrawableRes
import com.dart69.dartnews.R
import com.dart69.dartnews.destinations.ScaffoldNewsScreenDestination
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

enum class BottomBarDestinations(
    val direction: DirectionDestinationSpec,
    @DrawableRes val icon: Int,
) {
    Articles(ScaffoldNewsScreenDestination, R.drawable.ic_article)
}
