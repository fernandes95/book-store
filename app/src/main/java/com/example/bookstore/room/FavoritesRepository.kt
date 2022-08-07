package com.example.bookstore.room

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class FavoritesRepository(private val volumeDao: VolumeDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allVolumes: Flow<List<VolumeEntity>> = volumeDao.getAll()

    fun volumeExists(id : String) : Boolean {return volumeDao.verifyExistsById(id)}

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(volume: VolumeEntity) {
        volumeDao.insertVolume(volume)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(volume: VolumeEntity) {
        volumeDao.delete(volume)
    }
}