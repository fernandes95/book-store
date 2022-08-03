package com.example.bookstore.room

import com.example.bookstore.dto.VolumeDto

fun VolumeDto.Volume.toVolumeEntity() = VolumeEntity(
    id = this.id,
    title = this.volumeInfo?.title!!,
    thumb = this.volumeInfo.imageLinks?.thumbnail!!
)
