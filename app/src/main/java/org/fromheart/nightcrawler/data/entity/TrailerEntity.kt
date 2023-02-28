package org.fromheart.nightcrawler.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trailer")
data class TrailerEntity(
    @PrimaryKey
    val id: String,
    val url: String,
)
