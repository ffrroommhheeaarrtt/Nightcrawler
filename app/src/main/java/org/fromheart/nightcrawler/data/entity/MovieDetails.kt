package org.fromheart.nightcrawler.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

data class Actor(
    val id: String,
    val name: String,
    val character: String,
    val photo: String,
) {
    fun toList(): List<String> {
        return listOf(id, name, character, photo)
    }
}

@Entity(tableName = "movie_details")
data class MovieDetailsEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val year: Int,
    val month: Int,
    val day: Int,
    val rating: Double?,
    val poster: String,
    val genre: String,
    val country: String,
    val director: String,
    val scriptwriter: String,
    val duration: Int?,
    val logline: String,
    val actors: List<Actor>,
    val favorites: Boolean,
)
