package com.example.bookstore.ui.screens.favorites

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.di.DaggerAppComponent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

sealed interface FavUiState {
    data class Success(val favorites: List<VolumeDto.Volume>) : FavUiState
    object Error : FavUiState
    object Loading : FavUiState
}

class FavoritesViewModel : ViewModel() {
    var favUiState: FavUiState by mutableStateOf(FavUiState.Loading)
        private set

    @Inject
    lateinit var repository: VolumesRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        DaggerAppComponent.create().inject(this)
//        compositeDisposable.add(repository.fetchFavoritesFromDatabase())
//        getFavData()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getFavorites() {
//        repository.fetchFavoritesFromDatabase()
    }
}