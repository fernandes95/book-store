package com.example.bookstore.data.repositories

import com.example.bookstore.VolumesApplication
import com.example.bookstore.data.room.FavoriteEntity
import com.example.bookstore.di.DaggerAppComponent
import kotlinx.coroutines.flow.Flow

class OfflineRepository {

    init {
        DaggerAppComponent.create().inject(this)
    }

    private suspend fun insertFavorite(volume: FavoriteEntity) =
        VolumesApplication.database.favDao().insert(volume)

    private suspend fun deleteFavorite(volume: FavoriteEntity) =
        VolumesApplication.database.favDao().delete(volume)

    private fun getAllFavorites() : Flow<List<FavoriteEntity>> =
        VolumesApplication.database.favDao().getAll()

    private fun getFavorite(volumeId: String) : Flow<FavoriteEntity> =
        VolumesApplication.database.favDao().findById(volumeId)

    suspend fun insertFavoriteToDatabase(volume: FavoriteEntity) = insertFavorite(volume)
    suspend fun deleteFavoriteToDatabase(volume: FavoriteEntity) = deleteFavorite(volume)
    fun fetchFavoritesFromDatabase() = getAllFavorites()
    fun fetchFavoriteFromDatabase(volumeId: String) = getFavorite(volumeId)
}