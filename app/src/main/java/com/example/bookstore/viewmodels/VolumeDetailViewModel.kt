package com.example.bookstore.viewmodels

import androidx.lifecycle.*
import com.example.bookstore.data.api.dto.VolumeDto
import com.example.bookstore.data.api.dto.toVolumeEntity
import com.example.bookstore.data.api.VolumesRepository
import com.example.bookstore.data.room.FavoriteEntity
import com.example.bookstore.data.room.FavoriteRepository
import com.example.bookstore.di.DaggerAppComponent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class VolumeDetailViewModel : ViewModel() {

    var volumeId : String = ""
    var selectedVolume : VolumeDto.Volume? = null
    var isLoading : MutableLiveData<Boolean> = MutableLiveData(true)
    var isFavorite : MutableLiveData<Boolean> = MutableLiveData(false)
    var favorite : FavoriteEntity? = null

    @Inject
    lateinit var repository: FavoriteRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        DaggerAppComponent.create().inject(this)
        compositeDisposable.add(repository.fetchFavoritesFromDatabase())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getVolume(volumeId : String): LiveData<VolumeDto.Volume>? {
        this.volumeId = volumeId
        return VolumesRepository.getVolumeDetailApiCall(volumeId)
        isFavorite.value = false
    }

    fun setFavorite(){
        try {
            if(favorite != null && isFavorite.value == true) {
                repository?.delete(favorite!!)
                isFavorite.value = false
                favorite = null
            }
            else {
                var vol = selectedVolume?.toVolumeEntity()
                vol?.let {
                    repository?.insert(it)
                    isFavorite.value = true
                }
            }

        } catch (e: Exception) {
            // handler error
        }
        finally {
            repository.fetchFavoritesFromDatabase()
        }
    }
}