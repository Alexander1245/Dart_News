package com.dart69.dartnews.main.presentation.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.dart69.dartnews.NavGraphs
import com.dart69.dartnews.news.presentation.ui.BottomNavigationBar
import com.ramcosta.composedestinations.DestinationsNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    Scaffold(
        modifier = modifier,
        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier.fillMaxWidth(),
                navController = navController
            )
        }) { innerPadding ->
        DestinationsNavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navGraph = NavGraphs.root,
            navController = navController,
        )
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(modifier = Modifier.fillMaxSize())
}