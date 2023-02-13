package com.example.bookstore.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query("SELECT * FROM favorites_table")
    fun getAll(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites_table WHERE volume_id LIKE :volumeId LIMIT 1")
    fun findById(volumeId: String): Flow<FavoriteEntity>

    @Query("SELECT EXISTS (SELECT * FROM favorites_table WHERE volume_id LIKE :volumeId LIMIT 1)")
    fun verifyExistsById(volumeId: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(volume: FavoriteEntity)

    @Delete
    suspend fun delete(volume: FavoriteEntity)
}