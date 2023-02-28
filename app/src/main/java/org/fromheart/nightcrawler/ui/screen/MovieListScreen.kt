package org.fromheart.nightcrawler.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import org.fromheart.nightcrawler.R
import org.fromheart.nightcrawler.data.entity.MovieEntity
import org.fromheart.nightcrawler.ui.Destinations
import org.fromheart.nightcrawler.ui.theme.NightcrawlerTheme
import org.fromheart.nightcrawler.ui.view.MovieItem
import org.fromheart.nightcrawler.ui.view.MovieSearchField
import org.fromheart.nightcrawler.ui.viewmodel.MovieListViewModel
import org.fromheart.nightcrawler.util.MovieSorting
import org.fromheart.nightcrawler.util.ThemePreview
import org.fromheart.nightcrawler.util.keyboardAsState
import org.koin.androidx.compose.koinViewModel

@Composable
private fun MovieListScreen(
    modifier: Modifier = Modifier,
    searchText: String = "",
    movies: List<MovieEntity> = emptyList(),
    sorting: MovieSorting = MovieSorting.POPULARITY,
    listState: LazyListState = rememberLazyListState(),
    onSearchClick: KeyboardActionScope.() -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onMenuItemClick: (sorting: MovieSorting) -> Unit = {},
    onFavoritesFabClick: () -> Unit = {},
    movieItem: @Composable (MovieEntity) -> Unit
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column {
            MovieSearchField(
                searchText = searchText,
                sorting = sorting,
                onSearchClick = onSearchClick,
                onSearchTextChange = onSearchTextChange,
                onMenuItemClick = onMenuItemClick,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxWidth()
            )
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                items(items = movies, key = { it.id }) { movie ->
                    movieItem(movie)
                }
                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }
        Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier.padding(16.dp)) {
            FloatingActionButton(onClick = onFavoritesFabClick, shape = CircleShape) {
                Icon(
                    imageVector = Icons.Rounded.Favorite,
                    contentDescription = stringResource(R.string.button_favorites_movies),
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MovieListScreen(
    navController: NavHostController,
    listState: LazyListState,
    modifier: Modifier = Modifier,
    viewModel: MovieListViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val haptic = LocalHapticFeedback.current
    val isKeyboardOpen by keyboardAsState()
    val movies by viewModel.movieList.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.updateMovies()
    }

    LaunchedEffect(isKeyboardOpen) {
        if (!isKeyboardOpen) focusManager.clearFocus()
    }

    if (movies.isEmpty() && viewModel.searchText.isEmpty()) {
        LoadingScreen()
    } else {
        MovieListScreen(
            searchText = viewModel.searchText,
            movies = movies,
            sorting = viewModel.sorting,
            listState = listState,
            onSearchClick = {
                keyboardController?.hide()
            },
            onSearchTextChange = {
                viewModel.searchText = it
                scope.launch {
                    viewModel.searchMovie(it).join()
                    listState.animateScrollToItem(0)
                }
            },
            onMenuItemClick = {
                viewModel.sorting = it
                viewModel.sortMovies(it)
                scope.launch {
                    listState.animateScrollToItem(0)
                }
            },
            onFavoritesFabClick = {
                navController.navigate(Destinations.FAVORITES)
            },
            modifier = modifier
        ) { movie ->
            MovieItem(
                title = movie.title,
                year = movie.year,
                rating = movie.rating,
                ratingCount = movie.votes,
                poster = movie.poster,
                favorites = movie.favorites,
                onFavoritesButtonClick = {
                    viewModel.updateFavorites(movie)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                onItemClick = {
                    navController.navigate("${Destinations.MOVIE}?movieId=${movie.id}")
                },
            )
        }
    }
}

@ThemePreview
@Composable
fun MovieListScreenPreview() {
    val movies = List(10) {
        MovieEntity(
            id = "$it",
            title = "Nightcrawler",
            year = 2014,
            rank = it,
            rating = 7.8,
            votes = 558970,
            poster = "",
            favorites = false,
        )
    }

    NightcrawlerTheme {
        MovieListScreen(movies = movies) { movie ->
            MovieItem(
                title = movie.title,
                year = movie.year,
                rating = movie.rating,
                ratingCount = movie.votes,
                favorites = movie.favorites,
            )
        }
    }
}