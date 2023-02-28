package org.fromheart.nightcrawler.data.repository

import org.fromheart.nightcrawler.data.dao.MovieDao
import org.fromheart.nightcrawler.data.entity.MovieDetailsEntity
import org.fromheart.nightcrawler.data.entity.MovieEntity
import org.fromheart.nightcrawler.data.entity.TrailerEntity
import org.fromheart.nightcrawler.network.ImDbApiService

class MovieRepository(private val dao: MovieDao, private val imDbService: ImDbApiService) {

    suspend fun addMovies(movieList: List<MovieEntity>) {
        dao.insertMovieList(movieList)
    }

    suspend fun updateMovie(movie: MovieEntity) {
        dao.updateMovie(movie)
    }

    suspend fun updateMovie(movieList: List<MovieEntity>) {
        dao.updateMovieList(movieList)
    }

    suspend fun getMovie(id: String): MovieEntity? {
        return dao.getMovie(id)
    }

    suspend fun getMovies(): List<MovieEntity> {
        return dao.getMovieList()
    }

    suspend fun getFavoriteMovies(): List<MovieEntity> {
        return dao.getMovieList().filter { it.favorites }
    }

    suspend fun addMovieDetails(movieDetails: MovieDetailsEntity) {
        dao.insertMovieDetails(movieDetails)
    }

    suspend fun updateMovieDetails(movieDetails: MovieDetailsEntity) {
        dao.updateMovieDetails(movieDetails)
    }

    suspend fun updateMovieDetails(movieDetailsList: List<MovieDetailsEntity>) {
        dao.updateMovieDetailsList(movieDetailsList)
    }

    suspend fun getMoviesDetails(id: String): MovieDetailsEntity? {
        return dao.getMovieDetails(id)
    }

    suspend fun getMoviesDetails(): List<MovieDetailsEntity> {
        return dao.getMovieDetailsList()
    }

    suspend fun addTrailer(trailer: TrailerEntity) {
        dao.insertTrailer(trailer)
    }

    suspend fun getTrailer(id: String): TrailerEntity? {
        return dao.getTrailer(id)
    }

    suspend fun fetchMovies(): Result<List<MovieEntity>> {
        return try {
            imDbService.getMovieList().let { response ->
                if (response.errorMessage.isEmpty()) {
                    Result.success(response.items.map { it.toMovie() })
                } else {
                    Result.failure(Exception(response.errorMessage))
                }
            }
        } catch (exc: Exception) {
            Result.failure(Exception(exc.message))
        }
    }

    suspend fun fetchMovieDetails(movieId: String): Result<MovieDetailsEntity> {
        return try {
            imDbService.getMovieDetails(movieId).let { response ->
                if (response.errorMessage.isEmpty()) {
                    Result.success(response.toMovieDetails(dao))
                } else {
                    Result.failure(Exception(response.errorMessage))
                }
            }
        } catch (exc: Exception) {
            Result.failure(Exception(exc.message))
        }
    }

    suspend fun fetchTrailer(movieId: String): Result<TrailerEntity> {
        return try {
            imDbService.getTrailer(movieId).let { response ->
                if (response.errorMessage.isEmpty()) {
                    Result.success(response.toTrailer())
                } else {
                    Result.failure(Exception(response.errorMessage))
                }
            }
        } catch (exc: Exception) {
            Result.failure(Exception(exc.message))
        }
    }
}