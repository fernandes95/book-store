package com.example.bookstore.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bookstore.utils.DATABASE_NAME

@Database(entities = [FavoriteEntity::class], version = 2)
abstract class FavoriteDatabase : RoomDatabase() {

    abstract fun favDao(): FavoriteDao

    companion object {
        @Volatile // All threads have immediate access to this property
        private var instance: FavoriteDatabase? = null

        private val LOCK = Any() // Makes sure no threads making the same thing at the same time

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                FavoriteDatabase::class.java,
                DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }
    }
}