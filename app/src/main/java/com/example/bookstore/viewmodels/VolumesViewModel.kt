package com.example.bookstore.viewmodels

import androidx.lifecycle.ViewModel
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
        repository.fetchVolumesFromApi()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getFavData() {
        repository.fetchFavoritesFromDatabase()
    }
}