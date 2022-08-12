package com.example.bookstore.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.bookstore.data.api.dto.VolumeDto
import com.example.bookstore.data.api.VolumesRepository
import com.example.bookstore.data.room.FavoriteRepository
import com.example.bookstore.di.DaggerAppComponent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class VolumesViewModel : ViewModel() {

    @Inject
    lateinit var repository: FavoriteRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        DaggerAppComponent.create().inject(this)
        compositeDisposable.add(repository.fetchFavoritesFromDatabase())
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getListData(): LiveData<ArrayList<VolumeDto.Volume>>? {
        repository.fetchFavoritesFromDatabase()
        return VolumesRepository.getVolumesApiCall()
    }
}