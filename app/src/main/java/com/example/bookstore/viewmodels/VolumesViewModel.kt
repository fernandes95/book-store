package com.example.bookstore.viewmodels

import androidx.lifecycle.ViewModel
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.di.DaggerAppComponent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class VolumesViewModel : ViewModel() {

    lateinit var searchQuery : String

    @Inject
    lateinit var repository: VolumesRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        DaggerAppComponent.create().inject(this)
        compositeDisposable.add(repository.fetchFavoritesFromDatabase())
        //repository.fetchVolumesFromApi()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getFavData() {
        repository.fetchFavoritesFromDatabase()
    }

    //adding 1 to the startIndex so that I don't get the previous last item duplicated
    fun getMoreVolumes(startIndex : Int){
        repository.fetchVolumesFromApi(query = searchQuery, startIndex = startIndex.plus(1))
    }

    fun searchVolumes(query: String){
        searchQuery = query
        repository.fetchVolumesFromApi(query = query)
    }
}