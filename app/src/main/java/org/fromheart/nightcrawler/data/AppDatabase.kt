package org.fromheart.nightcrawler.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.fromheart.nightcrawler.data.converter.ActorConverter
import org.fromheart.nightcrawler.data.dao.MovieDao
import org.fromheart.nightcrawler.data.entity.MovieDetailsEntity
import org.fromheart.nightcrawler.data.entity.MovieEntity
import org.fromheart.nightcrawler.data.entity.TrailerEntity
import org.fromheart.nightcrawler.util.APP_DATABASE_NAME

@Database(
    entities = [MovieEntity::class, MovieDetailsEntity::class, TrailerEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(ActorConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    APP_DATABASE_NAME
                ).run {
                    fallbackToDestructiveMigration()
                    build()
                }
            }.also { instance = it }
        }
    }
}