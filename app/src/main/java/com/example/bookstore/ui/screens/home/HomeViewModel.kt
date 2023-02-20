package com.example.bookstore.ui.screens.home

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookstore.data.repositories.VolumesRepository
import com.example.bookstore.di.DaggerAppComponent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
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

    private val compositeDisposable by lazy { CompositeDisposable() }

    init {
        DaggerAppComponent.create().inject(this)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getUserLogged(context: Context){
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            uiState = try {
                val userLogged = getUser(context)
                HomeUiState.Success(userLogged?.displayName)
            } catch (e: IOException) {
                HomeUiState.Retry
            } catch (e: HttpException) {
                HomeUiState.Retry
            }
        }
    }

    fun logout(context: Context){
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            uiState = try {
                val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build()

                val client = GoogleSignIn.getClient(context, gso)
                client.signOut()

                val userLogged = getUser(context)

                if(userLogged == null)
                    HomeUiState.LoggedOut
                else
                    HomeUiState.Success(userLogged.displayName)
            } catch (e: IOException) {
                HomeUiState.Retry
            } catch (e: HttpException) {
                HomeUiState.Retry
            }
        }
    }

    private fun getUser(context: Context) : GoogleSignInAccount? {
        return GoogleSignIn.getLastSignedInAccount(context)
    }
}