package org.fromheart.nightcrawler.ui.screen

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.fromheart.nightcrawler.R
import org.fromheart.nightcrawler.data.entity.Actor
import org.fromheart.nightcrawler.data.entity.MovieDetailsEntity
import org.fromheart.nightcrawler.ui.activity.MainActivity
import org.fromheart.nightcrawler.ui.theme.NightcrawlerTheme
import org.fromheart.nightcrawler.ui.viewmodel.MovieDetailsViewModel
import org.fromheart.nightcrawler.util.ThemePreview
import org.fromheart.nightcrawler.util.formatMovieDuration
import org.koin.androidx.compose.koinViewModel

@Composable
private fun MovieDescription(option: String, value: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier.padding(vertical = 4.dp)) {
        Text(
            text = option,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun MovieDetailsScreen(
    movie: MovieDetailsEntity,
    trailerButtonEnabled: Boolean,
    modifier: Modifier = Modifier,
    onFavoritesButtonClick: () -> Unit = {},
    onTrailerButtonClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val posterWidth = 300
    val posterHeight = posterWidth / 2 * 3
    val photoWidth = 150
    val photoHeight = photoWidth / 2 * 3

    Surface(modifier = modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Box(modifier = Modifier.padding(vertical = 16.dp)) {
                if (inspectionMode) {
                    Icon(
                        imageVector = Icons.Filled.Image,
                        contentDescription = stringResource(R.string.poster),
                        modifier = Modifier
                            .width(posterWidth.dp)
                            .height(posterHeight.dp)
                    )
                } else {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(context).run {
                            data(movie.poster)
                            crossfade(true)
                            build()
                        },
                        contentDescription = stringResource(R.string.poster),
                        contentScale = ContentScale.Crop,
                        alpha = 0.7f,
                        modifier = Modifier
                            .width(posterWidth.dp)
                            .height(posterHeight.dp)
                    ) {
                        if (painter.state is AsyncImagePainter.State.Success) {
                            SubcomposeAsyncImageContent()
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(modifier = Modifier.size(100.dp))
                            }
                        }
                    }
                }
                IconButton(
                    onClick = onFavoritesButtonClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(12.dp)
                        .size(50.dp)
                ) {
                    if (movie.favorites) {
                        Icon(
                            imageVector = Icons.Rounded.Favorite,
                            contentDescription = stringResource(R.string.button_remove_from_favorites),
                            tint = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.FavoriteBorder,
                            contentDescription = stringResource(R.string.button_adding_to_favorites),
                            tint = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(10.dp))
            movie.rating?.let {
                MovieDescription(option = "${stringResource(R.string.rating)}:", value = "${movie.rating}")
            }
            MovieDescription(
                option = "${stringResource(R.string.release_date)}:",
                value = "${stringArrayResource(R.array.months)[movie.month]} ${movie.day}, ${movie.year}",
            )
            MovieDescription(option = "${stringResource(R.string.genre)}:", value = movie.genre)
            MovieDescription(option = "${stringResource(R.string.country)}:", value = movie.country)
            MovieDescription(option = "${stringResource(R.string.director)}:", value = movie.director)
            MovieDescription(option = "${stringResource(R.string.scriptwriter)}:", value = movie.scriptwriter)
            movie.duration?.let {
                MovieDescription(
                    option = "${stringResource(R.string.movie_duration)}:",
                    value = formatMovieDuration(movie.duration),
                )
            }
            Divider(thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = movie.logline,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            Divider(thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))
            Text(
                text = stringResource(R.string.cast),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .padding(vertical = 8.dp)
                    .align(Alignment.Start)
            )
            LazyRow(
                contentPadding = PaddingValues(8.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                items(items = movie.actors, key = { it.id }) { actor ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .height(photoHeight.times(1.5).dp)
                    ) {
                        if (inspectionMode) {
                            Icon(
                                imageVector = Icons.Filled.Face,
                                contentDescription = stringResource(R.string.photo),
                                modifier = Modifier
                                    .width(photoWidth.dp)
                                    .height(photoHeight.dp)
                            )
                        } else {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(context).run {
                                    data(actor.photo)
                                    crossfade(true)
                                    build()
                                },
                                contentDescription = stringResource(R.string.photo),
                                contentScale = ContentScale.Crop,
                                alpha = 0.7f,
                                modifier = Modifier
                                    .width(photoWidth.dp)
                                    .height(photoHeight.dp)
                                    .clip(RoundedCornerShape(10))
                            ) {
                                if (painter.state is AsyncImagePainter.State.Success) {
                                    SubcomposeAsyncImageContent()
                                } else {
                                    Box(contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator()
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = actor.name,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.widthIn(max = photoWidth.dp)
                        )
                        Text(
                            text = actor.character,
                            color = MaterialTheme.colorScheme.outline,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.widthIn(max = photoWidth.dp)
                        )
                    }
                }
            }
            Divider(thickness = 2.dp, modifier = Modifier.padding(vertical = 8.dp))
            Button(
                onClick = onTrailerButtonClick,
                enabled = trailerButtonEnabled,
                modifier = Modifier
                    .padding(vertical = 16.dp, horizontal = 30.dp)
                    .fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(imageVector = Icons.Outlined.Visibility, contentDescription = null)
                    Text(
                        text = stringResource(R.string.button_trailer),
                        style = MaterialTheme.typography.titleLarge,
                        textDecoration = TextDecoration.Underline,
                        letterSpacing = TextUnit(2f, TextUnitType.Sp),
                    )
                    Icon(imageVector = Icons.Outlined.Visibility, contentDescription = null)
                }
            }
        }
    }
}

@Composable
fun MovieDetailsScreen(
    movieId: String,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailsViewModel = koinViewModel(),
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val mainActivity = (context as MainActivity)
    val haptic = LocalHapticFeedback.current
    var trailerButtonEnabled by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        viewModel.loadMovie(movieId)
    }

    if (viewModel.movie == null) {
        LoadingScreen()
    } else {
        viewModel.movie?.let { movie ->
            MovieDetailsScreen(
                movie = movie,
                trailerButtonEnabled = trailerButtonEnabled,
                modifier = modifier,
                onFavoritesButtonClick = {
                    viewModel.updateFavorites(movie)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                },
                onTrailerButtonClick = {
                    scope.launch {
                        trailerButtonEnabled = false
                        viewModel.loadTrailer(movieId).join()
                        viewModel.trailer?.let {
                            mainActivity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                        }
                        delay(1000L)
                        trailerButtonEnabled = true
                    }
                },
            )
        }
    }
}

@ThemePreview
@Composable
fun MovieDetailsScreenPreview() {
    NightcrawlerTheme {
        MovieDetailsScreen(
            movie = MovieDetailsEntity(
                id = "id",
                title = "Nightcrawler",
                year = 2014,
                month = 8,
                day = 5,
                rating = 7.8,
                poster = "",
                genre = "Crime, Drama, Thriller",
                country = "USA",
                director = "Dan Gilroy",
                scriptwriter = "Dan Gilroy",
                duration = 117,
                logline = "When Louis Bloom, a con man desperate for work," +
                        " muscles into the world of L.A. crime journalism, " +
                        "he blurs the line between observer and participant to become the star of his own story.",
                actors = List(10) {
                    Actor(
                        id = "$it",
                        name = "Jake Gyllenhaal",
                        character = "Louis Bloom",
                        photo = "",
                    )
                },
                favorites = false,
            ),
            trailerButtonEnabled = true,
        )
    }
}