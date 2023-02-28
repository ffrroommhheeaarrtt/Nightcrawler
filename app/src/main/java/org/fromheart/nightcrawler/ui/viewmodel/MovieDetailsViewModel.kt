package org.fromheart.nightcrawler.ui.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.fromheart.nightcrawler.data.entity.MovieDetailsEntity
import org.fromheart.nightcrawler.data.repository.MovieRepository
import org.fromheart.nightcrawler.util.DEBUG_TAG

class MovieDetailsViewModel(private val repository: MovieRepository) : ViewModel() {

    var movie by mutableStateOf<MovieDetailsEntity?>(null)
        private set

    var trailer by mutableStateOf<String?>(null)
        private set

    fun updateFavorites(movieDetails: MovieDetailsEntity) = viewModelScope.launch {
        movie = movieDetails.copy(favorites = !movieDetails.favorites)
        repository.updateMovieDetails(movieDetails.copy(favorites = !movieDetails.favorites))
        repository.getMovie(movieDetails.id)?.let {
            repository.updateMovie(it.copy(favorites = !movieDetails.favorites))
        }
    }

    fun loadMovie(movieId: String) = viewModelScope.launch {
        repository.fetchMovieDetails(movieId).let { result ->
            result.onSuccess {
                movie = it
                repository.addMovieDetails(it)
            }
            result.onFailure {
                Log.e(DEBUG_TAG, "${it.message}")
                movie = repository.getMoviesDetails(movieId)
            }
        }
    }

    fun loadTrailer(movieId: String) = viewModelScope.launch {
        repository.fetchTrailer(movieId).let { result ->
            result.onSuccess {
                trailer = it.url
                repository.addTrailer(it)
            }
            result.onFailure {
                Log.e(DEBUG_TAG, "${it.message}")
                trailer = repository.getTrailer(movieId)?.url
            }
        }
    }
}