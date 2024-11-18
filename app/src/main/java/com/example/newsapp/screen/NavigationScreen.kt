package com.example.newsapp.screen

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.viewmodels.NewsViewModel

@Composable
fun NavigationScreen(
    newsViewModel: NewsViewModel,
) {

    val navController = rememberNavController()

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {

            if (currentRoute?.startsWith(Screen.ArticleScreen.route) == false) {
                BottomNavigationBar(navController)
            }

        }
    ) { innerPadding ->

        NavigationGraph(
            navController,
            Modifier.padding(innerPadding),
            newsViewModel
        )


    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {

    val navItemList = listOf(
        Screen.SearchScreen,
        Screen.HeadlineScreen,
        Screen.SettingScreen

    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route


    NavigationBar {

        navItemList.forEach { screen ->

            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    }

                },
                icon = { Icon(imageVector = screen.icon, contentDescription = screen.title) },
                label = {
                    Text(text = screen.title)
                }
            )

        }
    }

}


@Composable
fun NavigationGraph(
    navController: NavHostController,
    modifier: Modifier,
    newsViewModel: NewsViewModel
) {

    NavHost(navController, startDestination = Screen.HeadlineScreen.route, modifier = modifier) {

        composable(Screen.SearchScreen.route)
        {
            SearchScreen(newsViewModel)
            { url ->
                navController.navigate("${Screen.ArticleScreen.route}/${Uri.encode(url)}")
            }
        }

        composable(Screen.HeadlineScreen.route)
        {

            HeadlinesScreen(newsViewModel)
            { url ->
                navController.navigate("${Screen.ArticleScreen.route}/${Uri.encode(url)}")
            }
        }

        composable(Screen.SettingScreen.route)
        {

            SettingsScreen(
                newsViewModel
            )
        }

        composable("${Screen.ArticleScreen.route}/{url}",
            arguments = listOf(
                navArgument("url")
                {
                    type = NavType.StringType
                }
            )
        )
        {

            val encodedUrl = it.arguments?.getString("url")
            val url = encodedUrl?.let { enUrl -> Uri.decode(enUrl) } ?: ""
            NewsArticleScreen(url)
        }
    }

}

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    data object SearchScreen : Screen("Search", "Search", Icons.Default.Search)
    data object HeadlineScreen : Screen("Headline", "Headline", Icons.Default.Info)
    data object SettingScreen : Screen("Setting", "Setting", Icons.Default.Settings)
    data object ArticleScreen : Screen("Article", "Article", Icons.Default.Create)
}
