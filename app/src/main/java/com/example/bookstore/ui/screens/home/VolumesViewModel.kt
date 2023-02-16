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
    data class Success(val volumes: List<VolumeDto.Volume>, val isLoading: Boolean,
                       val isOnLimit: Boolean = false) : ListUiState
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

    private var localQuery: String = "android"

    private var isNewSearch: Boolean = false

    init {
        DaggerAppComponent.create().inject(this)
        getVolumes()
    }

    fun getVolumes() {
        viewModelScope.launch {
           uiState = ListUiState.Loading
           uiState = try {
               list = repository.fetchVolumesFromApi()

               val count = list.size
               val uniqueList = filterUniqueVolumes(list)
               val difference = count.minus(uniqueList.size)
               val limit = checkVolumesLimit(uniqueList.size, difference)

               ListUiState.Success(uniqueList, false, limit)
            } catch (e: IOException) {
               ListUiState.Retry
            } catch (e: HttpException) {
               ListUiState.Retry
            }
        }
    }

    fun searchVolumes(query: String) {
        viewModelScope.launch {
            isNewSearch = true
            uiState = ListUiState.Success(emptyList(), true)
            uiState = try {
                localQuery = query
                list.clear()
                list = repository.fetchVolumesFromApi(query)

                val count = list.size
                val uniqueList = filterUniqueVolumes(list)
                val difference = count.minus(uniqueList.size)
                val limit = checkVolumesLimit(uniqueList.size, difference)

                isNewSearch = false
                ListUiState.Success(uniqueList, false, limit)
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
        if(isNewSearch)
            return

        viewModelScope.launch {
            uiState = ListUiState.Success(list, true)
            uiState = try {
                val newVolumes = repository.fetchVolumesFromApi(localQuery, list.size)

                list.addAll(newVolumes)

                val uniqueList = filterUniqueVolumes(list)
                val limit = checkVolumesLimit(newVolumes.size)

                ListUiState.Success(uniqueList, false, limit)
            } catch (e: IOException) {
                ListUiState.Retry
            } catch (e: HttpException) {
                ListUiState.Retry
            }
        }
    }

    private fun filterUniqueVolumes(list: ArrayList<VolumeDto.Volume>) : ArrayList<VolumeDto.Volume>{
        val newList = arrayListOf<VolumeDto.Volume>()
        newList.addAll(list)

        val uniqueVolumes = newList.distinctBy { it.id }
        return ArrayList(uniqueVolumes)
    }

    /*
    api some times returns repeated id's on first query
    after filtering to unique id's the count will always be lower than 'API_MAX_RESULTS'
    this difference param ensures that we can still load more items on this occasions
    */
    private fun checkVolumesLimit(count: Int, difference: Int = 0) = count < (API_MAX_RESULTS.toInt().minus(difference))
}