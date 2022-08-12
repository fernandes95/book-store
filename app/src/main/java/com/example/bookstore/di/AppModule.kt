package com.example.bookstore.di

import com.example.bookstore.data.room.FavoriteRepository
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @Provides
    fun provideFavoriteRepository() = FavoriteRepository()
}