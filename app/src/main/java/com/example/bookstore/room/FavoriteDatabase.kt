package com.example.bookstore.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

const val DATABASE_NAME = "favoritesDb.db"

@Database(entities = [VolumeEntity::class],version = 1)
abstract class FavoriteDatabase:RoomDatabase(){
    abstract fun volumeDao(): VolumeDao

    companion object{
        private var instance: FavoriteDatabase?=null

        fun getInstance(context:Context): FavoriteDatabase{
            if (instance == null)
                instance = Room.databaseBuilder(context.applicationContext, FavoriteDatabase::class.java, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build()

            return instance!!
        }
    }
}

