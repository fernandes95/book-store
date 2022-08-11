package com.example.bookstore.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.example.bookstore.dto.VolumeDto
import com.example.bookstore.repository.VolumesRepository
import com.example.bookstore.room.*
import kotlinx.coroutines.launch

class VolumeDetailViewModel(app: Application) : AndroidViewModel(app) {

    private var repository : FavoritesRepository? = null

    val favoriteUid : Int? = null
    var selectedVolume : VolumeDto.Volume? = null
    private var volEntity : VolumeEntity? = null
    var isLoading : MutableLiveData<Boolean> = MutableLiveData(false)

    fun setFavorite(context: Context){
        isLoading.value = true

            try {
                if(volEntity != null)
                    repository?.delete(volEntity!!)
                else {
                    var vol = selectedVolume?.toVolumeEntity()
                    vol?.let { repository?.insert(it) }
                }

            } catch (e: Exception) {
                // handler error
            }
            finally {
                isLoading.value = false
            }

        //selectedVolume?.toVolumeEntity()?.let { FavoritesRepository.setFavorite(context, it) }

        /*if(isFav == true){
            FavoritesRepository.getAllVolumes(context)
            getVolumeDb(context)
            isLoading.value = false
        }*/

    }

    fun getVolume(volumeId : String): LiveData<VolumeDto.Volume>? {
        return VolumesRepository.getVolumeDetailApiCall(volumeId)
    }
}