package com.dart69.dartnews.main.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.dart69.dartnews.main.presentation.ui.MainScreen
import com.dart69.dartnews.main.presentation.ui.ThemeAndSurface
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThemeAndSurface(modifier = Modifier.fillMaxSize()) {
                MainScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}