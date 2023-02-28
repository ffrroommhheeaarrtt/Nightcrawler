package org.fromheart.nightcrawler.network

import org.fromheart.nightcrawler.data.dao.MovieDao
import org.fromheart.nightcrawler.data.entity.Actor
import org.fromheart.nightcrawler.data.entity.MovieDetailsEntity

data class ActorData(
    val id: String,
    val image: String,
    val name: String,
    val asCharacter: String,
)

data class MovieResponse(
    val errorMessage: String,
    val id: String,
    val releaseDate: String,
    val image: String,
    val genres: String,
    val countries: String,
    val directors: String,
    val writers: String,
    val runtimeMins: Int?,
    val plot: String,
    val actorList: List<ActorData>,
) {
    suspend fun toMovieDetails(dao: MovieDao): MovieDetailsEntity {
        val movie = dao.getMovie(id)!!
        val (year, month, day) = releaseDate.split("-").map(String::toInt)

        return MovieDetailsEntity(
            id = id,
            title = movie.title,
            year = year,
            month = month - 1,
            day = day,
            rating = movie.rating,
            poster = image,
            genre = genres,
            country = countries,
            director = directors,
            scriptwriter = writers,
            duration = runtimeMins,
            logline = plot,
            actors = actorList.map {
                Actor(
                    id = it.id,
                    name = it.name,
                    character = it.asCharacter,
                    photo = it.image,
                )
            },
            favorites = movie.favorites,
        )
    }
}
