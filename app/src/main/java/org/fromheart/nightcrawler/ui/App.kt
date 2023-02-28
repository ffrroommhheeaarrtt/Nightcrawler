package org.fromheart.nightcrawler.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.fromheart.nightcrawler.ui.screen.FavoriteMoviesScreen
import org.fromheart.nightcrawler.ui.screen.MovieDetailsScreen
import org.fromheart.nightcrawler.ui.screen.MovieListScreen
import org.fromheart.nightcrawler.ui.theme.NightcrawlerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val navController = rememberNavController()
    val movieListState = rememberLazyListState()

    NightcrawlerTheme {
        Scaffold { contentPadding ->
            NavHost(
                navController = navController,
                startDestination = Destinations.MOVIE_LIST,
                modifier = Modifier.padding(contentPadding)
            ) {
                composable(route = Destinations.MOVIE_LIST) {
                    MovieListScreen(navController = navController, listState = movieListState)
                }
                composable(route = Destinations.FAVORITES) {
                    FavoriteMoviesScreen(navController = navController)
                }
                composable(
                    route = "${Destinations.MOVIE}?movieId={movieId}",
                    arguments = listOf(
                        navArgument("movieId") {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    backStackEntry.arguments?.getString("movieId")?.let {
                        MovieDetailsScreen(movieId = it)
                    }
                }
            }
        }
    }
}

