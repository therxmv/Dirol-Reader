package com.therxmv.dirolreader.domain.models

data class MessageModel(
    val id: Long,
    val channelId: Long,
    val channelRating: Int,
    val channelName: String,
    val channelAvatarPath: String?,
    val timestamp: Int,
    val text: String,
    val photoPath: String?
)
