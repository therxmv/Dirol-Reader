package com.therxmv.dirolreader.domain.models

import org.drinkless.td.libcore.telegram.TdApi

data class MessageModel(
    var id: Long,
    val channelId: Long,
    val channelRating: Int,
    val channelName: String,
    val channelAvatarPath: String?,
    val timestamp: Int,
    var text: String,
    val photos: MutableList<TdApi.PhotoSize>?
)
