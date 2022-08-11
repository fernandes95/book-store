package com.example.bookstore.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface VolumeDao {
    @Query("SELECT * FROM favorites")
    fun getAll(): LiveData<List<VolumeEntity>>

    @Query("SELECT * FROM favorites WHERE volume_id LIKE :volumeId LIMIT 1")
    fun findById(volumeId: String): VolumeEntity

    @Query("SELECT EXISTS (SELECT * FROM favorites WHERE volume_id LIKE :volumeId LIMIT 1)")
    fun verifyExistsById(volumeId: String): Boolean

    @Insert
    fun insert(vararg volume: VolumeEntity)

    @Delete
    fun delete(volume: VolumeEntity)
}