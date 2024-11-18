package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.screen.NavigationScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import com.example.newsapp.viewmodels.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            val newsViewModel: NewsViewModel = hiltViewModel()

            val selectedTheme by newsViewModel.themeSet.collectAsState()

            NewsAppTheme(themeString = selectedTheme) {

                NavigationScreen(
                    newsViewModel
                )
            }


        }
    }

}



