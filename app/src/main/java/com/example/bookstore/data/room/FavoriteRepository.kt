package com.example.bookstore.data.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bookstore.VolumesApplication
import com.example.bookstore.data.api.VolumeApi
import com.example.bookstore.data.models.dto.VolumeDto
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

class FavoriteRepository {

    @Inject
    lateinit var volumeApi: VolumeApi

    init {
        DaggerAppComponent.create().inject(this)
    }

    private val _volumesData by lazy { MutableLiveData<List<VolumeDto.Volume>>() }
    val volumesData: LiveData<List<VolumeDto.Volume>>
        get() = _volumesData

    private val _favoritesData by lazy { MutableLiveData<List<FavoriteEntity>>() }
    val favoriteData: LiveData<List<FavoriteEntity>>
        get() = _favoritesData

    private val _isInProgress by lazy { MutableLiveData<Boolean>() }
    val isInProgress: LiveData<Boolean>
        get() = _isInProgress

    private val _isError by lazy { MutableLiveData<Boolean>() }
    val isError: LiveData<Boolean>
        get() = _isError


    fun insert(volume: FavoriteEntity) {
        subscribeOnBackground {
            VolumesApplication.database.favDao().insert(volume)
        }
    }

    fun delete(favorite: FavoriteEntity) {
        subscribeOnBackground {
            VolumesApplication.database.favDao().delete(favorite)
        }
    }

    private fun getVolumes(query : String, startIndex : Int) {
        _isInProgress.postValue(true)
        val call = volumeApi.getVolumesQuery(mapOf("q" to query,
                "maxResults" to API_MAX_RESULTS,
                "startIndex" to startIndex.toString()))

        call?.enqueue(object: Callback<VolumeDto.Volumes> {
            override fun onFailure(call: Call<VolumeDto.Volumes>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
                _isError.postValue(true)
                _isInProgress.postValue(false)
            }

            override fun onResponse(
                call: Call<VolumeDto.Volumes>,
                response: Response<VolumeDto.Volumes>
            ) {
                Log.v("DEBUG : ", response.body().toString())

                val list = response.body()?.items
                _volumesData.value = list?.let { ArrayList(list) }
                _isError.postValue(false)
                _isInProgress.postValue(false)
            }
        })
    }

    private fun getVolume(volumeId : String) : MutableLiveData<VolumeDto.Volume> {
        _isInProgress.postValue(true)

        val volume = MutableLiveData<VolumeDto.Volume>()
        val call = volumeApi.getVolumeById(volumeId)

        call.enqueue(object: Callback<VolumeDto.Volume> {
            override fun onFailure(call: Call<VolumeDto.Volume>, t: Throwable) {
                Log.v("DEBUG : ", t.message.toString())
                _isError.postValue(true)
                _isInProgress.postValue(false)
            }

            override fun onResponse(
                call: Call<VolumeDto.Volume>,
                response: Response<VolumeDto.Volume>
            ) {
                Log.v("DEBUG : ", response.body().toString())
                volume.value = response.body()
                _isError.postValue(false)
                _isInProgress.postValue(false)
            }
        })
        return volume
    }


    private fun getAllFavorites(): Disposable {
        return VolumesApplication.database.favDao()
            .getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { dataEntityList ->
                    _isInProgress.postValue(true)
                    if (dataEntityList != null && dataEntityList.isNotEmpty()) {
                        _isError.postValue(false)
                        _favoritesData.postValue(dataEntityList)
                    } else {
                        _favoritesData.postValue(emptyList())
                    }
                    _isInProgress.postValue(false)

                },
                {
                    _isInProgress.postValue(true)
                    Log.e("getAllFavorites()", "Database error: ${it.message}")
                    _isError.postValue(true)
                    _isInProgress.postValue(false)
                }
            )
    }

    fun fetchFavoritesFromDatabase(): Disposable = getAllFavorites()
    fun fetchVolumesFromApi(query : String = "ios", startIndex : Int = 0) = getVolumes(query, startIndex)
    fun fetchVolumeFromApi(volumeId : String) = getVolume(volumeId)
}