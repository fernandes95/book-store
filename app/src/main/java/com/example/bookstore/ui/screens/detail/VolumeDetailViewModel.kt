package com.example.bookstore.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.di.DaggerAppComponent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface DetailUiState {
    data class Success(val volume: VolumeDto.Volume) : DetailUiState
    object Error : DetailUiState
    object Loading : DetailUiState
}

class VolumeDetailViewModel : ViewModel() {

    var detailUiState: DetailUiState by mutableStateOf(DetailUiState.Loading)
        private set

    @Inject
    lateinit var repository: VolumesRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        DaggerAppComponent.create().inject(this)
//        compositeDisposable.add(repository.fetchFavoritesFromDatabase())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getVolume(volumeId : String) = viewModelScope.launch {
            detailUiState = DetailUiState.Loading
            detailUiState = try {
                DetailUiState.Success(repository.fetchVolumeFromApi(volumeId))
            } catch (e: IOException) {
                DetailUiState.Error
            } catch (e: HttpException) {
                DetailUiState.Error
            }
        }

    /*fun setFavorite(){
        try {
            if(favorite != null && isFavorite.value == true) {
                repository.delete(favorite!!)
                isFavorite.value = false
                favorite = null
            }
            else {
                val vol = selectedVolume?.toFavorite()
                vol?.let {
                    repository.insert(it)
                    isFavorite.value = true
                }
            }

        } catch (e: Exception) {
            //TODO
        }
        finally {
            repository.fetchFavoritesFromDatabase()
        }
    }*/
}