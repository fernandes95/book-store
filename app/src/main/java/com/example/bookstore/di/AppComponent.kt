package com.example.bookstore.di

import com.example.bookstore.viewmodels.VolumeDetailViewModel
import com.example.bookstore.viewmodels.VolumesViewModel
import com.example.bookstore.data.room.FavoriteRepository
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(favoriteRepository: FavoriteRepository)

    fun inject(viewModel: VolumesViewModel)

    fun inject(viewModel: VolumeDetailViewModel)
}