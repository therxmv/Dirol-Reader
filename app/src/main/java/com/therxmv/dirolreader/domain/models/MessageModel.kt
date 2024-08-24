package com.therxmv.dirolreader.domain.models

data class MessageModel(
    val id: Long,
    val channelData: ChannelData,
    val timestamp: Int,
    val text: String,
    val mediaList: List<MediaModel>?,
)

data class ChannelData(
    val id: Long,
    val rating: Int,
    val name: String,
    val avatarPath: String? = null,
)