package org.fromheart.nightcrawler.network

import org.fromheart.nightcrawler.data.entity.MovieEntity

data class MoviesDataDetail(
    val id: String,
    val title: String,
    val year: Int,
    val rank: Int,
    val imDbRating: String,
    val imDbRatingCount: Int,
    val image: String,
) {
    fun toMovie(): MovieEntity {
        return MovieEntity(
            id = id,
            title = title,
            year = year,
            rank = rank,
            rating = imDbRating.toDoubleOrNull(),
            votes = imDbRatingCount,
            poster = image,
        )
    }
}

data class MoviesResponse(val errorMessage: String, val items: List<MoviesDataDetail>)