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
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val volumes: List<VolumeDto.Volume>) : HomeUiState
    object Error : HomeUiState
    object Loading : HomeUiState
}

class VolumesViewModel : ViewModel() {

    var homeUiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    @Inject
    lateinit var repository: VolumesRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    private var list : ArrayList<VolumeDto.Volume> = arrayListOf()

    init {
        DaggerAppComponent.create().inject(this)
        getVolumes()
    }

    fun getVolumes() {
        viewModelScope.launch {
           homeUiState = HomeUiState.Loading
           homeUiState = try {
               list = repository.fetchVolumesFromApi()
               HomeUiState.Success(list)
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
        viewModelScope.launch {
            homeUiState = HomeUiState.Loading
            homeUiState = try {
                val newVolumes = repository.fetchVolumesFromApi(startIndex = startIndex.plus(1))
                list.addAll(newVolumes)
                val newList = arrayListOf<VolumeDto.Volume>()
                newList.addAll(list)
                HomeUiState.Success(newList)
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: HttpException) {
                HomeUiState.Error
            }
        }
    }
}