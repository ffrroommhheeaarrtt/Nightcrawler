package org.fromheart.nightcrawler.di

import org.fromheart.nightcrawler.data.AppDatabase
import org.fromheart.nightcrawler.data.repository.MovieRepository
import org.fromheart.nightcrawler.network.ImDbApiService
import org.fromheart.nightcrawler.ui.viewmodel.FavoriteMoviesViewModel
import org.fromheart.nightcrawler.ui.viewmodel.MovieDetailsViewModel
import org.fromheart.nightcrawler.ui.viewmodel.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import retrofit2.create

val appModule = module {
    single { MovieRepository(AppDatabase.getDatabase(get()).movieDao(), ImDbApiService.getService().create()) }

    viewModelOf(::MovieListViewModel)
    viewModelOf(::FavoriteMoviesViewModel)
    viewModelOf(::MovieDetailsViewModel)
}