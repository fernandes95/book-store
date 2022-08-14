package com.example.bookstore

import android.app.Application
import com.example.bookstore.data.room.FavoriteDatabase

class VolumesApplication : Application() {

    companion object {
        lateinit var instance: VolumesApplication
        lateinit var database: FavoriteDatabase
    }

    init {
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        database = FavoriteDatabase.invoke(this)
    }
}