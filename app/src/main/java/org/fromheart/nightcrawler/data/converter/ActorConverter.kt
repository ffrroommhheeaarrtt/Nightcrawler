package org.fromheart.nightcrawler.data.converter

import androidx.room.TypeConverter
import org.fromheart.nightcrawler.data.entity.Actor

private const val ACTOR_DELIMITER = "\n"
private const val VALUE_DELIMITER = "|"

object ActorConverter {

    @TypeConverter
    fun fromList(list: List<Actor>): String {
        return list.joinToString(ACTOR_DELIMITER) { it.toList().joinToString(VALUE_DELIMITER) }
    }

    @TypeConverter
    fun fromString(str: String): List<Actor> {
        return str.split(ACTOR_DELIMITER).map {
            val (id, name, character, photo) = it.split(VALUE_DELIMITER)
            Actor(
                id = id,
                name = name,
                character = character,
                photo = photo
            )
        }
    }
}