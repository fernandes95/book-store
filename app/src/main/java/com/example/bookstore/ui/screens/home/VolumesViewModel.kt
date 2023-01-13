package com.example.bookstore.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.di.DaggerAppComponent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val volumes: VolumeDto.Volumes) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}

class VolumesViewModel : ViewModel() {

    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    @Inject
    lateinit var repository: VolumesRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        DaggerAppComponent.create().inject(this)
//        compositeDisposable.add(repository.fetchFavoritesFromDatabase())
        getVolumes()
    }

    fun getVolumes() {
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            homeUiState = try {
                HomeUiState.Success(repository.fetchVolumesFromApi())
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: HttpException) {
                HomeUiState.Error
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    //adding 1 to the startIndex so that I don't get the previous last item duplicated
    fun getMoreVolumes(startIndex : Int){
        //repository.fetchVolumesFromApi(startIndex = startIndex.plus(1))
    }
}