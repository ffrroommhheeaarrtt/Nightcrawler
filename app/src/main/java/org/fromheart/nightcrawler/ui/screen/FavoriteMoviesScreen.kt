package org.fromheart.nightcrawler.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import org.fromheart.nightcrawler.ui.viewmodel.FavoriteMoviesViewModel
import org.fromheart.nightcrawler.util.MovieSorting
import org.fromheart.nightcrawler.util.ThemePreview
import org.fromheart.nightcrawler.util.keyboardAsState
import org.koin.androidx.compose.koinViewModel

@Composable
private fun FavoriteMoviesScreen(
    modifier: Modifier = Modifier,
    searchText: String = "",
    movies: List<MovieEntity> = emptyList(),
    sorting: MovieSorting = MovieSorting.POPULARITY,
    listState: LazyListState = rememberLazyListState(),
    onSearchClick: KeyboardActionScope.() -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onMenuItemClick: (sorting: MovieSorting) -> Unit = {},
    onBackButtonClick: () -> Unit = {},
    onCleanFavoritesFab: () -> Unit = {},
    movieItem: @Composable LazyItemScope.(MovieEntity) -> Unit
) {
    Surface(modifier = modifier.fillMaxSize()) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBackButtonClick) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = stringResource(R.string.button_back))
                }
                MovieSearchField(
                    searchText = searchText,
                    sorting = sorting,
                    onSearchClick = onSearchClick,
                    onSearchTextChange = onSearchTextChange,
                    onMenuItemClick = onMenuItemClick,
                    modifier = Modifier
                        .padding(vertical = 8.dp, horizontal = 4.dp)
                        .weight(1f)
                )
            }
            LazyColumn(
                state = listState,
                contentPadding = PaddingValues(8.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(items = movies, key = { it.id }) { movie ->
                    movieItem(movie)
                }
                item {
                    Spacer(modifier = Modifier.height(70.dp))
                }
            }
        }
        Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.padding(16.dp)) {
            FloatingActionButton(onClick = onCleanFavoritesFab) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = stringResource(R.string.button_cleaning_favorites),
                )
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class)
@Composable
fun FavoriteMoviesScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: FavoriteMoviesViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val haptic = LocalHapticFeedback.current
    val isKeyboardOpen by keyboardAsState()
    val listState = rememberLazyListState()
    val movies by viewModel.movieList.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.updateMovies()
    }

    LaunchedEffect(isKeyboardOpen) {
        if (!isKeyboardOpen) focusManager.clearFocus()
    }

    FavoriteMoviesScreen(
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
        onBackButtonClick = {
            navController.navigateUp()
        },
        onCleanFavoritesFab = {
            viewModel.cleanFavorites()
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            navController.navigateUp()
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
                viewModel.deleteFromFavorites(movie)
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            },
            onItemClick = {
                navController.navigate("${Destinations.MOVIE}?movieId=${movie.id}")
            },
            modifier = Modifier
                .animateItemPlacement(spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioLowBouncy))
        )
    }
}

@ThemePreview
@Composable
fun FavoriteMoviesScreenPreview() {
    val movies = List(10) {
        MovieEntity(
            id = "$it",
            title = "Nightcrawler",
            year = 2014,
            rank = it,
            rating = 7.8,
            votes = 558970,
            poster = "",
            favorites = true,
        )
    }

    NightcrawlerTheme {
        FavoriteMoviesScreen(movies = movies) { movie ->
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