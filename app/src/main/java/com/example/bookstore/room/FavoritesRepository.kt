package com.example.bookstore.room

import com.example.bookstore.utils.subscribeOnBackground
import android.app.Application
import androidx.lifecycle.LiveData

class FavoritesRepository(application: Application) {

    private var volDao: VolumeDao
    private var allFavorites: LiveData<List<VolumeEntity>>

    private val database = FavoriteDatabase.getInstance(application)

    init {
        volDao = database.volumeDao()
        allFavorites = volDao.getAll()
    }

    fun findById(volumeId: String): VolumeEntity{
        return volDao.findById(volumeId)
    }

    fun verifyExistsById(volumeId: String): Boolean{
        return volDao.verifyExistsById(volumeId)
    }

    fun insert(volume: VolumeEntity) {
        subscribeOnBackground {
            volDao.insert(volume)
        }
    }

    fun delete(volume: VolumeEntity) {
        subscribeOnBackground {
            volDao.delete(volume)
        }
    }

    fun getAll(): LiveData<List<VolumeEntity>> {
        return allFavorites
    }
}
