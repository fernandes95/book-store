package com.example.bookstore.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites_table")
data class FavoriteEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int = 0,
    @ColumnInfo(name = "volume_id") val id: String,
    @ColumnInfo(name = "volume_title") val title: String,
    @ColumnInfo(name = "volume_thumb") val thumb: String?
)