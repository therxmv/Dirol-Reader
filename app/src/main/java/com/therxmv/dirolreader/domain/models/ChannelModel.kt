package com.therxmv.dirolreader.domain.models

data class ChannelModel(
    val id: Long,
    val title: String,
    val avatarId: Int?,
    val rating: Int = 0,
)
