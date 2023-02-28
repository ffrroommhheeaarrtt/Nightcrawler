package org.fromheart.nightcrawler.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val year: Int,
    val rank: Int,
    val rating: Double?,
    val votes: Int,
    val poster: String,
    val favorites: Boolean = false,
)