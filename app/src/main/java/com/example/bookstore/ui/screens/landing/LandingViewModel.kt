package com.example.bookstore.ui.screens.landing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.repositories.GoogleRepository
import com.example.bookstore.di.DaggerAppComponent
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

sealed interface LandingUiState {
    object Success : LandingUiState
    object NoAccount : LandingUiState
    object Loading : LandingUiState
    object Retry : LandingUiState
}

class LandingViewModel: ViewModel() {

    var uiState: LandingUiState by mutableStateOf(LandingUiState.Loading)
        private set

    @Inject
    lateinit var googleRepository: GoogleRepository

    init {
        DaggerAppComponent.create().inject(this)
        checkSignedInUser()
    }

    private fun checkSignedInUser() {
        viewModelScope.launch {
            uiState = LandingUiState.Loading
            uiState = try {
                val user = googleRepository.getUser()
                if(user != null)
                    LandingUiState.Success
                else
                    LandingUiState.NoAccount
            } catch (e: IOException) {
                LandingUiState.Retry
            } catch (e: HttpException) {
                LandingUiState.Retry
            }
        }
    }
}