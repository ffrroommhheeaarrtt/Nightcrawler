package org.fromheart.nightcrawler.network

import org.fromheart.nightcrawler.data.entity.TrailerEntity

data class TrailerResponse(
    val errorMessage: String,
    val imDbId: String,
    val videoUrl: String,
) {
    fun toTrailer(): TrailerEntity {
        return TrailerEntity(id = imDbId, url = videoUrl)
    }
}
