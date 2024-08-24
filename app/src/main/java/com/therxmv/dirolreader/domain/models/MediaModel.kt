package com.therxmv.dirolreader.domain.models

data class MediaModel(
    val id: Int,
    val height: Int,
    val width: Int,
    val sizeInMb: String,
    val type: MediaType,
)

enum class MediaType {
    PHOTO,
    VIDEO,
}
