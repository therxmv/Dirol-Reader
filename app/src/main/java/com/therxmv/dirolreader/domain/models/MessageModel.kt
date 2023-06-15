package com.therxmv.dirolreader.domain.models

import org.drinkless.td.libcore.telegram.TdApi

data class MessageModel(
    val id: Long,
    val channelId: Long,
    val channelRating: Int,
    val channelName: String,
    val channelAvatarPath: String?,
    val timestamp: Int,
    val text: String,
    val photo: TdApi.PhotoSize?
)
