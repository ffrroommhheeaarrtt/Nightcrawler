package org.fromheart.nightcrawler.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.fromheart.nightcrawler.data.entity.MovieEntity
import org.fromheart.nightcrawler.data.repository.MovieRepository
import org.fromheart.nightcrawler.util.MovieSorting

class FavoriteMoviesViewModel(private val repository: MovieRepository) : ViewModel() {

    var searchText by mutableStateOf("")

    var sorting by mutableStateOf(MovieSorting.POPULARITY)

    private val _movieList = MutableStateFlow(emptyList<MovieEntity>())
    val movieList = _movieList.asStateFlow()

    init {
        viewModelScope.launch {
            _movieList.value = repository.getFavoriteMovies()
        }
    }

    private fun List<MovieEntity>.sortMovies(sorting: MovieSorting): List<MovieEntity> {
        return when (sorting) {
            MovieSorting.POPULARITY -> this.sortedBy { it.rank }
            MovieSorting.RATING -> this.sortedByDescending { it.rating }
            MovieSorting.VOTES -> this.sortedByDescending { it.votes }
            MovieSorting.TITLE -> this.sortedBy { it.title }
        }
    }

    fun updateMovies() {
        searchMovie(searchText)
    }

    fun deleteFromFavorites(movie: MovieEntity) = viewModelScope.launch {
        _movieList.value = movieList.value - movie
        repository.updateMovie(movie.copy(favorites = false))
        repository.getMoviesDetails(movie.id)?.let {
            repository.updateMovieDetails(it.copy(favorites = false))
        }
    }

    fun cleanFavorites() = viewModelScope.launch {
        _movieList.value = emptyList()
        repository.getFavoriteMovies().let { list ->
            repository.updateMovie(list.map { it.copy(favorites = false) })
        }
        repository.getMoviesDetails().filter { it.favorites }.let { list ->
            repository.updateMovieDetails(list.map { it.copy(favorites = false) })
        }
    }

    fun sortMovies(sorting: MovieSorting) {
        _movieList.value = movieList.value.sortMovies(sorting)
    }

    fun searchMovie(text: String) = viewModelScope.launch {
        text.trim().lowercase().let { str ->
            if (str.isNotEmpty()) {
                _movieList.value = repository.getFavoriteMovies()
                    .filter { it.title.lowercase().startsWith(str) }
                    .sortMovies(sorting)
            } else {
                _movieList.value = repository.getFavoriteMovies().sortMovies(sorting)
            }
        }
    }
}