package org.fromheart.nightcrawler.ui.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Sort
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import org.fromheart.nightcrawler.R
import org.fromheart.nightcrawler.ui.theme.NightcrawlerTheme
import org.fromheart.nightcrawler.util.MovieSorting
import org.fromheart.nightcrawler.util.ThemePreview

@Composable
private fun MovieSortingItem(
    text: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    DropdownMenuItem(
        text = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = text)
                Spacer(modifier = Modifier.width(4.dp))
                RadioButton(selected = selected, onClick = onClick)
            }
        },
        onClick = onClick,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieSearchField(
    modifier: Modifier = Modifier,
    searchText: String = "",
    sorting: MovieSorting = MovieSorting.POPULARITY,
    onSearchClick: KeyboardActionScope.() -> Unit = {},
    onSearchTextChange: (String) -> Unit = {},
    onMenuItemClick: (sorting: MovieSorting) -> Unit = {},
) {
    var menuExpanded by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        placeholder = {
            Text(
                text = stringResource(R.string.placeholder_movie_search),
                color = MaterialTheme.colorScheme.outline,
            )
        },
        shape = RoundedCornerShape(25),
        leadingIcon = {
            Icon(imageVector = Icons.Filled.Search, contentDescription = null)
        },
        trailingIcon = {
            IconButton(onClick = { menuExpanded = true }) {
                Icon(
                    imageVector = Icons.Rounded.Sort,
                    contentDescription = stringResource(R.string.sorting),
                )
            }
            DropdownMenu(expanded = menuExpanded, onDismissRequest = { menuExpanded = false }) {
                Text(
                    text = stringResource(R.string.sorted_by),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Divider(
                    thickness = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                )
                MovieSortingItem(
                    text = stringResource(R.string.popularity),
                    selected = sorting == MovieSorting.POPULARITY,
                    onClick = {
                        onMenuItemClick(MovieSorting.POPULARITY)
                        menuExpanded = false
                    },
                )
                MovieSortingItem(
                    text = stringResource(R.string.rating),
                    selected = sorting == MovieSorting.RATING,
                    onClick = {
                        onMenuItemClick(MovieSorting.RATING)
                        menuExpanded = false
                    },
                )
                MovieSortingItem(
                    text = stringResource(R.string.number_of_ratings),
                    selected = sorting == MovieSorting.VOTES,
                    onClick = {
                        onMenuItemClick(MovieSorting.VOTES)
                        menuExpanded = false
                    },
                )
                MovieSortingItem(
                    text = stringResource(R.string.movie_title),
                    selected = sorting == MovieSorting.TITLE,
                    onClick = {
                        onMenuItemClick(MovieSorting.TITLE)
                        menuExpanded = false
                    },
                )
            }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            capitalization = KeyboardCapitalization.Sentences
        ),
        keyboardActions = KeyboardActions(
            onSearch = onSearchClick
        ),
        textStyle = MaterialTheme.typography.bodyLarge,
        singleLine = true,
        modifier = modifier
    )
}

@ThemePreview
@Composable
fun MovieSearchFieldPreview() {
    NightcrawlerTheme {
        Surface {
            MovieSearchField(modifier = Modifier.padding(8.dp))
        }
    }
}