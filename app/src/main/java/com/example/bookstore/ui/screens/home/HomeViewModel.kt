package com.example.bookstore.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.repositories.GoogleRepository
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.di.DaggerAppComponent
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface HomeUiState {
    data class Success(val username: String?) : HomeUiState
    object LoggedOut : HomeUiState
    object Retry : HomeUiState
    object Loading : HomeUiState
}

class HomeViewModel : ViewModel() {

    var uiState: HomeUiState by mutableStateOf(HomeUiState.Loading)
        private set

    @Inject
    lateinit var repository: VolumesRepository

    @Inject
    lateinit var googleRepository: GoogleRepository

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        DaggerAppComponent.create().inject(this)
        getUserLogged()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun getUserLogged() {
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            uiState = try {
                val userLogged = googleRepository.getUser()
                HomeUiState.Success(userLogged?.displayName)
            } catch (e: IOException) {
                HomeUiState.Retry
            } catch (e: HttpException) {
                HomeUiState.Retry
            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            try {
                val userLoggedOut = googleRepository.logoutUser()
                userLoggedOut.collect{
                    uiState = if(it) {
                        HomeUiState.LoggedOut
                    } else {
                        HomeUiState.Success(googleRepository.googleUser?.displayName)
                    }
                }
            } catch (e: IOException) {
                HomeUiState.Retry
            } catch (e: HttpException) {
                HomeUiState.Retry
            }
        }
    }
}