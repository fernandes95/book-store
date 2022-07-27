package com.example.bookstore.dto

import com.fasterxml.jackson.annotation.JsonProperty

class VolumeDto {

    data class Volumes (
        @JsonProperty("kind") val kind: String?,
        @JsonProperty("totalItems") val totalItems: Int?,
        @JsonProperty("items") val items: ArrayList<Volume>?
    )

    data class Volume (
        @JsonProperty("kind") val kind: String?,
        @JsonProperty("id") val id: String?,
        @JsonProperty("etag") val etag: String?,
        @JsonProperty("selfLink") val selfLink: String?,
        @JsonProperty("volumeInfo") val volumeInfo: VolumeInfo?,
        @JsonProperty("saleInfo") val saleInfo: SaleInfo?,
        @JsonProperty("accessInfo") val accessInfo: AccessInfo?,
        @JsonProperty("searchInfo") val searchInfo: SearchInfo?,
    )

    data class VolumeInfo (
        @JsonProperty("title") val title: String?,
        @JsonProperty("subtitle") val subtitle: String?,
        @JsonProperty("authors") val authors: ArrayList<String>,
        @JsonProperty("publisher") val publisher: String?,
        @JsonProperty("publishedDate") val publishedDate: String?,
        @JsonProperty("description") val description: String?,
        @JsonProperty("industryIdentifiers") val industryIdentifiers: ArrayList<IndustryIdentifier>?,
        @JsonProperty("readingModes") val readingModes: ReadingModes?,
        @JsonProperty("pageCount") val pageCount : Int?,
        @JsonProperty("printType") val printType: String?,
        @JsonProperty("categories") val categories: ArrayList<String>?,
        @JsonProperty("averageRating") val averageRating : Int?,
        @JsonProperty("ratingsCount") val ratingsCount : Int?,
        @JsonProperty("maturityRating") val maturityRating: String?,
        @JsonProperty("allowAnonLogging") val allowAnonLogging : Boolean,
        @JsonProperty("contentVersion") val contentVersion: String?,
        @JsonProperty("panelizationSummary") val panelizationSummary: PanelizationSummary?,
        @JsonProperty("imageLinks") val imageLinks: ImageLinks?,
        @JsonProperty("language") val language: String?,
        @JsonProperty("previewLink") val previewLink: String?,
        @JsonProperty("infoLink") val infoLink: String?,
        @JsonProperty("canonicalVolumeLink") val canonicalVolumeLink: String
    )

    data class AccessInfo (
        @JsonProperty("country") val country: String?,
        @JsonProperty("viewability") val viewability: String?,
        @JsonProperty("embeddable") val embeddable : Boolean,
        @JsonProperty("publicDomain") val publicDomain : Boolean,
        @JsonProperty("textToSpeechPermission") val textToSpeechPermission: String?,
        @JsonProperty("epub") val epub: Epub?,
        @JsonProperty("pdf") val pdf: Pdf?,
        @JsonProperty("webReaderLink") val webReaderLink: String?,
        @JsonProperty("accessViewStatus") val accessViewStatus: String?,
        @JsonProperty("quoteSharingAllowed") val quoteSharingAllowed : Boolean,
    )

    data class Epub (
        @JsonProperty("isAvailable") val isAvailable : Boolean,
        @JsonProperty("acsTokenLink") val acsTokenLink: String?
    )

    data class ImageLinks (
        @JsonProperty("smallThumbnail") val smallThumbnail: String?,
        @JsonProperty("thumbnail") val thumbnail: String?,
    )

    data class IndustryIdentifier (
        @JsonProperty("type") val type: String?,
        @JsonProperty("identifier") val identifier: String?,
    )

    data class ListPrice (
        @JsonProperty("amount") val amount : Float?,
        @JsonProperty("currencyCode") val currencyCode: String?,
        @JsonProperty("amountInMicros") val amountInMicros : Int?
    )

    data class Offer (
        @JsonProperty("finskyOfferType") val finskyOfferType : Int?,
        @JsonProperty("listPrice") val listPrice: ListPrice?,
        @JsonProperty("retailPrice") val retailPrice: RetailPrice?,
    )

    data class PanelizationSummary (
        @JsonProperty("containsEpubBubbles") val containsEpubBubbles : Boolean,
        @JsonProperty("containsImageBubbles") val containsImageBubbles : Boolean
    )

    data class Pdf (
        @JsonProperty("isAvailable") val isAvailable : Boolean,
        @JsonProperty("acsTokenLink") val acsTokenLink: String?
    )

    data class ReadingModes (
        @JsonProperty("text") val text : Boolean,
        @JsonProperty("image") val image : Boolean
    )

    data class RetailPrice (
        @JsonProperty("amount") val amount : Float?,
        @JsonProperty("currencyCode") val currencyCode: String?,
        @JsonProperty("amountInMicros") val amountInMicros : Int?
    )

    data class SaleInfo (
        @JsonProperty("country") val country: String?,
        @JsonProperty("saleability") val saleability: String?,
        @JsonProperty("isEbook") val isEbook : Boolean,
        @JsonProperty("listPrice") val listPrice: ListPrice?,
        @JsonProperty("retailPrice") val retailPrice: RetailPrice?,
        @JsonProperty("buyLink") val buyLink: String?,
        @JsonProperty("offers") val offers: ArrayList<Offer>?,
    )

    data class SearchInfo (
        @JsonProperty("textSnippet") val textSnippet: String?
    )
}