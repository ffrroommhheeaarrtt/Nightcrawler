package org.fromheart.nightcrawler.data.dao

import androidx.room.*
import org.fromheart.nightcrawler.data.entity.MovieDetailsEntity
import org.fromheart.nightcrawler.data.entity.MovieEntity
import org.fromheart.nightcrawler.data.entity.TrailerEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieList(movieList: List<MovieEntity>)

    @Update
    suspend fun updateMovie(movie: MovieEntity)

    @Update
    suspend fun updateMovieList(movieList: List<MovieEntity>)

    @Query("select * from movie where :id == id")
    suspend fun getMovie(id: String): MovieEntity?

    @Query("select * from movie")
    suspend fun getMovieList(): List<MovieEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieDetails(movieDetails: MovieDetailsEntity)

    @Update
    suspend fun updateMovieDetails(movieDetails: MovieDetailsEntity)

    @Update
    suspend fun updateMovieDetailsList(movieDetailList: List<MovieDetailsEntity>)

    @Query("select * from movie_details where :id == id")
    suspend fun getMovieDetails(id: String): MovieDetailsEntity?

    @Query("select * from movie_details")
    suspend fun getMovieDetailsList(): List<MovieDetailsEntity>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrailer(trailerEntity: TrailerEntity)

    @Query("select * from trailer where :id == id")
    suspend fun getTrailer(id: String): TrailerEntity?
}