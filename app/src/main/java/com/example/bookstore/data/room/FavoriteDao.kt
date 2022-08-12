package com.example.bookstore.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites_table")
    fun getAll(): Single<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites_table WHERE volume_id LIKE :volumeId LIMIT 1")
    fun findById(volumeId: String): LiveData<FavoriteEntity>

    @Query("SELECT EXISTS (SELECT * FROM favorites_table WHERE volume_id LIKE :volumeId LIMIT 1)")
    fun verifyExistsById(volumeId: String): Boolean

    @Insert
    fun insert(volume: FavoriteEntity)

    @Delete
    fun delete(volume: FavoriteEntity)
}