package org.fromheart.nightcrawler.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.StarRate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.request.ImageRequest
import org.fromheart.nightcrawler.R
import org.fromheart.nightcrawler.ui.theme.NightcrawlerTheme
import org.fromheart.nightcrawler.util.ThemePreview
import org.fromheart.nightcrawler.util.formatRatingCount

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieItem(
    title: String,
    year: Int,
    rating: Double?,
    ratingCount: Int,
    favorites: Boolean,
    modifier: Modifier = Modifier,
    poster: String = "",
    onFavoritesButtonClick: () -> Unit = {},
    onItemClick: () -> Unit = {},
) {
    val context = LocalContext.current
    val inspectionMode = LocalInspectionMode.current
    val posterWidth = 150
    val posterHeight = posterWidth / 2 * 3

    ElevatedCard(
        onClick = onItemClick,
        modifier = modifier
            .height(posterHeight.dp)
            .widthIn(max = 500.dp)
            .fillMaxWidth()
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(vertical = 8.dp, horizontal = 4.dp)
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )
                    Text(
                        text = "($year)",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
                if (rating != null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.width(posterWidth.dp)
                        ) {
                            Text(
                                text = "$rating",
                                style = MaterialTheme.typography.displaySmall,
                                fontWeight = FontWeight.Bold,
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Icon(
                                imageVector = Icons.Rounded.StarRate,
                                contentDescription = stringResource(R.string.rating),
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.CenterVertically)
                            )
                        }
                        Text(text = formatRatingCount(ratingCount))
                    }
                }
            }
            Box {
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
                            data(poster)
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
                                CircularProgressIndicator()
                            }
                        }
                    }
                }
                IconButton(onClick = onFavoritesButtonClick, modifier = Modifier.align(Alignment.TopEnd)) {
                    if (favorites) {
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
        }
    }
}

@ThemePreview
@Composable
fun MovieItemPreview() {
    NightcrawlerTheme {
        Surface {
            MovieItem(
                title = "Nightcrawler",
                year = 2014,
                rating = 7.8,
                ratingCount = 558970,
                favorites = true,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}