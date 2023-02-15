package com.example.bookstore.ui.screens.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.models.toVolume
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.data.room.FavoriteEntity
import com.example.bookstore.di.DaggerAppComponent
import com.example.bookstore.ui.screens.home.ListUiState
import com.example.bookstore.utils.TIMEOUT_MILLIS
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

class FavoritesViewModel : ViewModel() {

    lateinit var uiState: StateFlow<ListUiState>

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
         uiState = repository.fetchFavoritesFromDatabase().map {
            if(it.any()) {
                val list = convertEntityList(it)
                ListUiState.Success(list, false)
            }
            else
                ListUiState.Retry
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = ListUiState.Loading
        )
    }

    private fun convertEntityList(list : List<FavoriteEntity>) : ArrayList<VolumeDto.Volume> {
        val listConverted = ArrayList<VolumeDto.Volume>()
        list.forEach { listConverted.add(it.toVolume())}

        return listConverted
    }
}