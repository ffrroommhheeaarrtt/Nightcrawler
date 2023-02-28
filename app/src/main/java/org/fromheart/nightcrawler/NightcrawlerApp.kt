package org.fromheart.nightcrawler

import android.app.Application
import android.util.Log
import kotlinx.coroutines.runBlocking
import org.fromheart.nightcrawler.data.repository.MovieRepository
import org.fromheart.nightcrawler.di.appModule
import org.fromheart.nightcrawler.util.DEBUG_TAG
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class NightcrawlerApp : Application() {

    private val movieRepository: MovieRepository by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(applicationContext)
            modules(appModule)
        }

        runBlocking {
            movieRepository.fetchMovies().let { result ->
                result.onSuccess {
                    movieRepository.addMovies(it)
                }
                result.onFailure {
                    Log.e(DEBUG_TAG, "${it.message}")
                }
            }
        }
    }
}