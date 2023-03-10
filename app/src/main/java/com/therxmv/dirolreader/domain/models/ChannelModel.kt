package com.therxmv.dirolreader.domain.models

data class ChannelModel(
    val id: Long = 0,
    val avatarPath: Int? = null,
    val channelName: String = "",
    val lastMessageText: String = "",
    val lastMessageDate: Int = 0,
    val photoPath: Int? = null,
    val rating: Int = 0,
)
