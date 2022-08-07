package com.example.bookstore.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

const val DATABASE_NAME = "favorites.db"

@Database(entities = [VolumeEntity::class], version = 1)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun volumeDao(): VolumeDao

    private class FavoriteDatabaseCallback(
        private val scope: CoroutineScope) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.volumeDao())
                }
            }
        }

        suspend fun populateDatabase(volumeDao: VolumeDao) {
//            // Delete all content here.
//            volumeDao.deleteAll()
//
//            // Add sample words.
//            var word = Word("Hello")
//            volumeDao.insert(word)
//            word = Word("World!")
//            volumeDao.insert(word)
//
//            // TODO: Add your own words!
        }
    }

    companion object {

        @Volatile
        private var INSTANCE: FavoriteDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): FavoriteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FavoriteDatabase::class.java,
                    DATABASE_NAME
                )
                .addCallback(FavoriteDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

