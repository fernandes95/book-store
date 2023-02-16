package com.example.bookstore.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.models.toFavorite
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.data.room.FavoriteEntity
import com.example.bookstore.di.DaggerAppComponent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface DetailUiState {
    data class Success(val volume: VolumeDto.Volume, val isFav: Boolean) : DetailUiState
    object Error : DetailUiState
    object Loading : DetailUiState
}

class VolumeDetailViewModel : ViewModel() {

    var uiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    @Inject
    lateinit var repository: VolumesRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    lateinit var volume: VolumeDto.Volume
    private var volumeFromDb: FavoriteEntity? = null

    init {
        DaggerAppComponent.create().inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getVolume(volumeId : String) = viewModelScope.launch {
            uiState = DetailUiState.Loading
            uiState = try {
                volume = repository.fetchVolumeFromApi(volumeId)
                volumeFromDb = repository.fetchFavoriteFromDatabase(volumeId).first()
                DetailUiState.Success(volume, volumeFromDb != null)
            } catch (e: IOException) {
                DetailUiState.Error
            } catch (e: HttpException) {
                DetailUiState.Error
            }
        }

    fun setFavorite() = viewModelScope.launch {
            if(volumeFromDb != null) {
                val volume = volumeFromDb
                repository.deleteFavoriteToDatabase(volume!!)
            }
            else {
                repository.insertFavoriteToDatabase(volume.toFavorite())
            }

            uiState = try {
                volumeFromDb = repository.fetchFavoriteFromDatabase(volume.id).first()
                DetailUiState.Success(volume, volumeFromDb != null)
            } catch (e: IOException) {
                DetailUiState.Error
            } catch (e: HttpException) {
                DetailUiState.Error
            }
        }
}