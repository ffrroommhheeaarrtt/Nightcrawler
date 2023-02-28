package org.fromheart.nightcrawler.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.fromheart.nightcrawler.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

private const val BASE_URL = "https://imdb-api.com/api/"
private const val API_KEY = BuildConfig.IMDB_API_KEY

interface ImDbApiService {

    @GET("MostPopularMovies/$API_KEY")
    suspend fun getMovieList(): MoviesResponse

    @GET("Title/$API_KEY/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") movieId: String): MovieResponse

    @GET("YouTubeTrailer/$API_KEY/{movie_id}")
    suspend fun getTrailer(@Path("movie_id") movieId: String): TrailerResponse

    companion object {
        fun getService(): Retrofit = Retrofit.Builder().run {
            val moshi = Moshi.Builder().run {
                add(KotlinJsonAdapterFactory())
                build()
            }

            baseUrl(BASE_URL)
            addConverterFactory(MoshiConverterFactory.create(moshi))
            build()
        }
    }
}