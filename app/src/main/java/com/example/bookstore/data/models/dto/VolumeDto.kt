package com.example.bookstore.data.models.dto

class VolumeDto {//TODO REVISE GOOGLE MODEL DOCUMENTATION TO CHECK WHICH FIELDS ARE EXPECTED TO BE NULL

    data class Volumes (
        val kind: String?,
        val totalItems: Int?,
        val items: ArrayList<Volume>?
    )

    data class Volume (
        val kind: String,
        val id: String,
        val etag: String,
        val selfLink: String?,
        val volumeInfo: VolumeInfo?,
        val saleInfo: SaleInfo?,
        val accessInfo: AccessInfo?,
        val searchInfo: SearchInfo?,
    )

    data class VolumeInfo (
        val title: String?,
        val subtitle: String?,
        val authors: ArrayList<String>,
        val publisher: String?,
        val publishedDate: String?,
        val description: String?,
        val industryIdentifiers: ArrayList<IndustryIdentifier>?,
        val readingModes: ReadingModes?,
        val pageCount : Int?,
        val printType: String?,
        val categories: ArrayList<String>?,
        val averageRating : Float?,
        val ratingsCount : Int?,
        val maturityRating: String?,
        val allowAnonLogging : Boolean,
        val contentVersion: String?,
        val panelizationSummary: PanelizationSummary?,
        val imageLinks: ImageLinks?,
        val language: String?,
        val previewLink: String?,
        val infoLink: String?,
        val canonicalVolumeLink: String
    )

    data class AccessInfo (
        val country: String?,
        val viewability: String?,
        val embeddable : Boolean,
        val publicDomain : Boolean,
        val textToSpeechPermission: String?,
        val epub: Epub?,
        val pdf: Pdf?,
        val webReaderLink: String?,
        val accessViewStatus: String?,
        val quoteSharingAllowed : Boolean,
    )

    data class Epub (
        val isAvailable : Boolean,
        val acsTokenLink: String?
    )

    data class ImageLinks (
        val smallThumbnail: String?,
        val thumbnail: String?,
    )

    data class IndustryIdentifier (
        val type: String?,
        val identifier: String?,
    )

    data class ListPrice (
        val amount : Float?,
        val currencyCode: String?,
        val amountInMicros : Int?
    )

    data class Offer (
        val finskyOfferType : Int?,
        val listPrice: ListPrice?,
        val retailPrice: RetailPrice?,
    )

    data class PanelizationSummary (
        val containsEpubBubbles : Boolean,
        val containsImageBubbles : Boolean
    )

    data class Pdf (
        val isAvailable : Boolean,
        val acsTokenLink: String?
    )

    data class ReadingModes (
        val text : Boolean,
        val image : Boolean
    )

    data class RetailPrice (
        val amount : Float?,
        val currencyCode: String?,
        val amountInMicros : Int?
    )

    data class SaleInfo (
        val country: String?,
        val saleability: String?,
        val isEbook : Boolean,
        val listPrice: ListPrice?,
        val retailPrice: RetailPrice?,
        val buyLink: String?,
        val offers: ArrayList<Offer>?,
    )

    data class SearchInfo (
        val textSnippet: String?
    )
}