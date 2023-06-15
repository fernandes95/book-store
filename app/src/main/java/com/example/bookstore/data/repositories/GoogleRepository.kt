package com.example.bookstore.data.repositories

import com.example.bookstore.VolumesApplication
import com.example.bookstore.di.DaggerAppComponent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class GoogleRepository {

    lateinit var googleUser: GoogleSignInAccount

    init {
        DaggerAppComponent.create().inject(this)
    }

    fun getUser() : GoogleSignInAccount? {
        var user: GoogleSignInAccount? = null
        val gsa = GoogleSignIn.getLastSignedInAccount(VolumesApplication.instance.applicationContext)

        if (gsa != null) {
            googleUser = gsa
            user = gsa
            /*user = GoogleUser(
                email = gsa.email,
                name = gsa.displayName,
            )*/
        }

        return user
    }

    fun logoutUser(): Boolean {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        val client = GoogleSignIn.getClient(VolumesApplication.instance.applicationContext, gso)
        client.signOut()

        return getUser() == null
    }

}