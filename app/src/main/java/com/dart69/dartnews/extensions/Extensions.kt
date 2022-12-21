package com.dart69.dartnews.extensions

import androidx.navigation.NavDestination
import com.dart69.dartnews.news.domain.model.Screen

fun Sequence<NavDestination>?.isSelected(screen: Screen) : Boolean =
    this?.any { navDestination -> navDestination.route == screen.route } == true