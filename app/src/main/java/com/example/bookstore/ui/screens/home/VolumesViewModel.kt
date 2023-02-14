package com.example.bookstore.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.di.DaggerAppComponent
import com.example.bookstore.utils.API_MAX_RESULTS
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val volumes: List<VolumeDto.Volume>, val isLoading: Boolean, val isOnLimit: Boolean = false) : HomeUiState
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
               val limit = checkVolumesLimit(list.size)
               HomeUiState.Success(list, false, limit)
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

    fun getMoreVolumes(){
        viewModelScope.launch {
            homeUiState = HomeUiState.Success(list, true)
            homeUiState = try {
                val newVolumes = repository.fetchVolumesFromApi(startIndex = list.count())

                list.addAll(newVolumes)

                val newList = arrayListOf<VolumeDto.Volume>()
                newList.addAll(list)

                 val uniqueVolumes = newList.distinctBy { it.id }
                 list = ArrayList(uniqueVolumes)

                val limit = checkVolumesLimit(newVolumes.size)
                HomeUiState.Success(list, false, limit)
            } catch (e: IOException) {
                HomeUiState.Error
            } catch (e: HttpException) {
                HomeUiState.Error
            }
        }
    }

    private fun checkVolumesLimit(count: Int) = count < API_MAX_RESULTS.toInt()
}