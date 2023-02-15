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

sealed interface ListUiState {
    data class Success(val volumes: List<VolumeDto.Volume>, val isLoading: Boolean, val isOnLimit: Boolean = false) : ListUiState
    object Retry : ListUiState
    object Loading : ListUiState
}

class VolumesViewModel : ViewModel() {

    var uiState: ListUiState by mutableStateOf(ListUiState.Loading)
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
           uiState = ListUiState.Loading
           uiState = try {
               list = repository.fetchVolumesFromApi()
               val limit = checkVolumesLimit(list.size)
               ListUiState.Success(list, false, limit)
            } catch (e: IOException) {
               ListUiState.Retry
            } catch (e: HttpException) {
               ListUiState.Retry
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getMoreVolumes(){
        viewModelScope.launch {
            uiState = ListUiState.Success(list, true)
            uiState = try {
                val newVolumes = repository.fetchVolumesFromApi(startIndex = list.count())

                list.addAll(newVolumes)

                val newList = arrayListOf<VolumeDto.Volume>()
                newList.addAll(list)

                 val uniqueVolumes = newList.distinctBy { it.id }
                 list = ArrayList(uniqueVolumes)

                val limit = checkVolumesLimit(newVolumes.size)
                ListUiState.Success(list, false, limit)
            } catch (e: IOException) {
                ListUiState.Retry
            } catch (e: HttpException) {
                ListUiState.Retry
            }
        }
    }

    private fun checkVolumesLimit(count: Int) = count < API_MAX_RESULTS.toInt()
}