package com.example.bookstore.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.di.DaggerAppComponent
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val username: String) : HomeUiState
    object Retry : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel : ViewModel() {

    var uiState: HomeUiState by mutableStateOf(HomeUiState.Success("test"))
        private set

    @Inject
    lateinit var repository: VolumesRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        DaggerAppComponent.create().inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}