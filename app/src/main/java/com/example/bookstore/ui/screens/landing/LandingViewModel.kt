package com.example.bookstore.ui.screens.landing

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.models.dto.GoogleUser
import com.example.bookstore.di.DaggerAppComponent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface LandingUiState {
    data class Success(val user: GoogleUser?) : LandingUiState
    object NoAccount : LandingUiState
    object Loading : LandingUiState
    object Retry : LandingUiState
}

class LandingViewModel: ViewModel() {

    var uiState: LandingUiState by mutableStateOf(LandingUiState.Loading)
        private set

    init {
        DaggerAppComponent.create().inject(this)
    }

    fun checkSignedInUser(applicationContext: Context) {
        viewModelScope.launch {
            uiState = LandingUiState.Loading
            uiState = try {
                var user: GoogleUser? = null
                val gsa = GoogleSignIn.getLastSignedInAccount(applicationContext)

                if (gsa != null) {
                    user = GoogleUser(
                        email = gsa.email,
                        name = gsa.displayName,
                    )
                }
                if(user != null)
                    LandingUiState.Success(user)
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