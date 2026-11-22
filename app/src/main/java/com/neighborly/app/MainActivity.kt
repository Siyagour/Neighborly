package com.neighborly.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.neighborly.app.ui.navigation.AppNavigation
import com.neighborly.app.ui.theme.NeighborlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = (application as NeighborlyApplication).repository

        setContent {
            NeighborlyTheme {
                AppNavigation(repository = repository)
            }
        }
    }
}

