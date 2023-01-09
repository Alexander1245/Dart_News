package com.dart69.dartnews.news.presentation.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dart69.dartnews.NavGraphs
import com.dart69.dartnews.appCurrentDestinationAsState
import com.dart69.dartnews.news.presentation.BottomBarDestinations
import com.dart69.dartnews.startAppDestination
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.navigation.popUpTo
import com.ramcosta.composedestinations.utils.isRouteOnBackStack

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    directions: List<BottomBarDestinations> = BottomBarDestinations.values().toList(),
    navController: NavController,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val currentDestination = navController.appCurrentDestinationAsState().value
        ?: NavGraphs.root.startAppDestination

    NavigationBar(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor
    ) {
        directions.forEach { destination ->
            NavigationBarItem(
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                selected = navController.isRouteOnBackStack(currentDestination),
                onClick = {
                    navController.navigate(destination.direction) {
                        popUpTo(destination.direction) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = destination.icon),
                        contentDescription = destination.name,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                label = {
                    SmallText(text = destination.name)
                }
            )
        }
    }
}

@Preview
@Composable
fun BottomNavigationBarPreview() {
    BottomNavigationBar(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth(),
        navController = rememberNavController()
    )
}