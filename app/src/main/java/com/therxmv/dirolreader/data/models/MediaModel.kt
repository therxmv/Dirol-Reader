package com.therxmv.dirolreader.data.models

data class MediaModel(
    val id: Int,
    val height: Int,
    val width: Int,
    val type: MediaType,
)
// TODO add isDownloaded
enum class MediaType {
    PHOTO,
    VIDEO,
}
