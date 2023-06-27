package com.therxmv.dirolreader.data.models

data class MediaModel(
    val id: Int,
    val height: Int,
    val width: Int,
    val size: Int,
    val type: MediaType,
)

enum class MediaType {
    PHOTO,
    VIDEO,
}
