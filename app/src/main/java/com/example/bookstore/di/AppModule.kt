package com.example.bookstore.di

import com.example.bookstore.data.api.ApiService
import com.example.bookstore.data.api.VolumeApi
import com.example.bookstore.data.room.FavoriteRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun provideApi(): VolumeApi = ApiService.getClient()

    @Provides
    fun provideFavoriteRepository() = FavoriteRepository()
}