package com.example.bookstore.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

const val DATABASE_NAME = "favorites.db"

@Database(entities = [VolumeEntity::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun volumeDao(): VolumeDao

    companion object {

        @Volatile // All threads have immediate access to this property
        private var instance: FavoriteDatabase? = null
        private val LOCK = Any() // Makes sure no threads making the same thing at the same time

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                FavoriteDatabase::class.java,
                DATABASE_NAME
            ).build()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }
}