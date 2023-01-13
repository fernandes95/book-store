package com.example.bookstore.data.models

import com.example.bookstore.data.models.dto.VolumeDto
import com.example.bookstore.data.room.FavoriteEntity

fun VolumeDto.Volume.toFavorite() = FavoriteEntity(
    id = this.id,
    title = this.volumeInfo?.title!!,
    thumb = this.volumeInfo.imageLinks?.thumbnail
)

fun FavoriteEntity.toVolume() = VolumeDto.Volume(
    id = this.id,
    kind = "",
    etag = "",
    selfLink = null,
    accessInfo = null,
    searchInfo = null,
    saleInfo = null,
    volumeInfo = VolumeDto.VolumeInfo(
        title = this.title,
        subtitle = null,
        allowAnonLogging = false,
        authors = arrayListOf(),
        canonicalVolumeLink = "",
        averageRating = null,
        categories = null,
        contentVersion = null,
        description = null,
        infoLink = null,
        language = null,
        industryIdentifiers = null,
        maturityRating = null,
        pageCount = null,
        previewLink = null,
        printType = null,
        panelizationSummary = null,
        publishedDate = null,
        publisher = null,
        ratingsCount = null,
        readingModes = null,
        imageLinks = VolumeDto.ImageLinks(null, this.thumb))
)
