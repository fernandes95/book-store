package com.example.bookstore.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface VolumeDao {
    @Query("SELECT * FROM favorites")
    fun getAll(): Flow<List<VolumeEntity>>

    @Query("SELECT * FROM favorites WHERE volume_id LIKE :volumeId LIMIT 1")
    fun findById(volumeId: String): VolumeEntity

    @Query("SELECT EXISTS (SELECT * FROM favorites WHERE volume_id LIKE :volumeId LIMIT 1)")
    fun verifyExistsById(volumeId: String): Boolean

    @Insert
    fun insertVolume(vararg volume: VolumeEntity)

    @Delete
    fun delete(volume: VolumeEntity)
}