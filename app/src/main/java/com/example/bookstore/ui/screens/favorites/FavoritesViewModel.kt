package com.example.bookstore.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.models.toVolume
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.data.room.FavoriteEntity
import com.example.bookstore.di.DaggerAppComponent
import com.example.bookstore.utils.TIMEOUT_MILLIS
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

sealed interface FavUiState {
    data class Success(val favorites: List<VolumeDto.Volume>) : FavUiState
    object Retry : FavUiState
    object Loading : FavUiState
}

class FavoritesViewModel : ViewModel() {

    lateinit var favUiState: StateFlow<FavUiState>

    @Inject
    lateinit var repository: VolumesRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        DaggerAppComponent.create().inject(this)
        getFavorites()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

     fun getFavorites(){
         favUiState = repository.fetchFavoritesFromDatabase().map {
            if(it.any()) {
                val list = convertEntityList(it)
                FavUiState.Success(list)
            }
            else
                FavUiState.Retry
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = FavUiState.Loading
        )
    }

    private fun convertEntityList(list : List<FavoriteEntity>) : ArrayList<VolumeDto.Volume> {
        val listConverted = ArrayList<VolumeDto.Volume>()
        list.forEach { listConverted.add(it.toVolume())}

        return listConverted
    }
}