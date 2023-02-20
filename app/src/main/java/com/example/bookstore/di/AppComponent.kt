package com.example.bookstore.di

import com.example.bookstore.data.repositories.OfflineRepository
import com.example.bookstore.ui.screens.detail.VolumeDetailViewModel
import com.example.bookstore.ui.screens.home.HomeViewModel
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.ui.screens.favorites.FavoritesViewModel
import com.example.bookstore.ui.screens.landing.LandingViewModel
import com.example.bookstore.ui.screens.search.SearchViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(repository: VolumesRepository)

    fun inject(repository: OfflineRepository)

    fun inject(viewModel: HomeViewModel)

    fun inject(viewModel: SearchViewModel)

    fun inject(viewModel: VolumeDetailViewModel)

    fun inject(viewModel: FavoritesViewModel)

    fun inject(viewModel: LandingViewModel)
}