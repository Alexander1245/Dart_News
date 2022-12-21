package com.dart69.dartnews.news.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.dart69.dartnews.extensions.isSelected
import com.dart69.dartnews.news.domain.model.Screen
import com.dart69.dartnews.ui.theme.Typography

@Composable
fun NewsBottomNavigation(
    modifier: Modifier = Modifier,
    screens: List<Screen> = Screen.findAll(),
    navController: NavController = rememberNavController(),
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    BottomNavigation(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination
        screens.forEach { screen ->
            BottomNavigationItem(
                selected = currentDestination?.hierarchy.isSelected(screen),
                icon = {
                    Icon(
                        painter = painterResource(id = screen.iconId),
                        contentDescription = stringResource(id = screen.contentDescriptionId),
                        tint = contentColor
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = screen.labelId),
                        style = Typography.labelSmall,
                        color = contentColor
                    )
                },
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }

                },
            )
        }
    }
}

@Preview
@Composable
fun NewsBottomNavigationPreview() {
    NewsBottomNavigation(modifier = Modifier
        .wrapContentHeight()
        .fillMaxWidth())
}