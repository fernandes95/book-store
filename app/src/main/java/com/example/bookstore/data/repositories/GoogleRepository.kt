package com.example.bookstore.data.repositories

import com.example.bookstore.VolumesApplication
import com.example.bookstore.di.DaggerAppComponent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class GoogleRepository {

    var googleUser: GoogleSignInAccount? = null

    init {
        DaggerAppComponent.create().inject(this)
    }

    fun getUser() : GoogleSignInAccount? {
        var user: GoogleSignInAccount? = null
        val gsa = GoogleSignIn.getLastSignedInAccount(VolumesApplication.instance.applicationContext)

        if (gsa != null) {
            googleUser = gsa
            user = gsa
        }

        return user
    }

     fun logoutUser() : Flow<Boolean> = callbackFlow {
        val client = GoogleSignIn.getClient(
          VolumesApplication.instance.applicationContext,
          GoogleSignInOptions.DEFAULT_SIGN_IN
        )

        client.signOut().addOnCompleteListener{
          if(it.isComplete) {
            googleUser = null
            this.trySend(true).isSuccess
          }
        }

       awaitClose()
    }

}