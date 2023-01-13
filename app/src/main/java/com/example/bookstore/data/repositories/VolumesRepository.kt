package com.example.bookstore.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookstore.VolumesApplication
import com.example.bookstore.data.api.VolumeApi
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.room.FavoriteEntity
import com.example.bookstore.di.DaggerAppComponent
import com.example.bookstore.utils.API_MAX_RESULTS
import com.example.bookstore.utils.subscribeOnBackground
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class VolumesRepository {

    @Inject
    lateinit var volumeApi: VolumeApi

    init {
        DaggerAppComponent.create().inject(this)
    }

    fun insert(volume: FavoriteEntity) {
        subscribeOnBackground {
            VolumesApplication.database.favDao().insert(volume)
            Log.v("DEBUG : ", "${volume.title}+ was added to db.")
        }
    }

    fun delete(favorite: FavoriteEntity) {
        subscribeOnBackground {
            VolumesApplication.database.favDao().delete(favorite)
            Log.v("DEBUG : ", "${favorite.title}+ was removed from db.")
        }
    }

    private suspend fun getVolumes(query : String, startIndex : Int) : VolumeDto.Volumes  =
        volumeApi.getVolumesQuery(
            mapOf(
                "q" to query,
                "maxResults" to API_MAX_RESULTS,
                "startIndex" to startIndex.toString()
            )
        )

    private suspend fun getVolume(volumeId : String) : VolumeDto.Volume =
        volumeApi.getVolumeById(volumeId)

    /*private fun getAllFavorites(): Disposable {
        return VolumesApplication.database.favDao()
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dataEntityList ->
//                    _isInProgress.postValue(true)
                    if (dataEntityList != null && dataEntityList.isNotEmpty()) {
                        _isError.postValue(false)
                        _favoritesData.postValue(ArrayList(dataEntityList))
                    } else
                        _favoritesData.postValue(ArrayList())

                    _isInProgress.postValue(false)

                },
                {
                    _isInProgress.postValue(true)
                    Log.e("getAllFavorites()", "Database error: ${it.message}")
                    _isError.postValue(true)
                    _isInProgress.postValue(false)
                }
            )
    }*/

//    fun fetchFavoritesFromDatabase(): Disposable = getAllFavorites()
    suspend fun fetchVolumesFromApi(query : String = "android", startIndex : Int = 0) = getVolumes(query, startIndex)
    suspend fun fetchVolumeFromApi(volumeId : String) = getVolume(volumeId)
}