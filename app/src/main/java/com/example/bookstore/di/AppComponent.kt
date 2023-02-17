package com.example.bookstore.di

import com.example.bookstore.data.repositories.OfflineRepository
import com.example.bookstore.ui.screens.detail.VolumeDetailViewModel
import com.example.bookstore.ui.screens.home.VolumesViewModel
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.ui.screens.favorites.FavoritesViewModel
import com.example.bookstore.ui.screens.landing.LandingViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(repository: VolumesRepository)

    fun inject(repository: OfflineRepository)

    fun inject(viewModel: VolumesViewModel)

    fun inject(viewModel: VolumeDetailViewModel)

    fun inject(viewModel: FavoritesViewModel)

    fun inject(viewModel: LandingViewModel)
}