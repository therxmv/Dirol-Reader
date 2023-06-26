package com.therxmv.dirolreader.domain.models

import com.therxmv.dirolreader.data.models.MediaModel

data class MessageModel(
    var id: Long,
    val channelId: Long,
    val channelRating: Int,
    val channelName: String,
    val channelAvatarPath: String?,
    val timestamp: Int,
    var text: String,
    val mediaList: MutableList<MediaModel>?
)
