package com.dart69.dartnews.main.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dart69.dartnews.news.domain.model.Screen
import com.dart69.dartnews.news.presentation.NewsBottomNavigation
import com.dart69.dartnews.news.presentation.ScaffoldNewsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = {
            NewsBottomNavigation(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                navController = navController
            )
        }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.News.route,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(Screen.News.route) { ScaffoldNewsScreen(modifier = Modifier.fillMaxSize()) }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(modifier = Modifier.fillMaxSize())
}