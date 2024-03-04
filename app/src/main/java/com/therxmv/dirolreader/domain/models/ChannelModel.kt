package com.therxmv.dirolreader.domain.models

import com.therxmv.dirolreader.data.entity.ChannelEntity

data class ChannelModel(
    val id: Long,
    val unreadCount: Int,
    val lastReadMessageId: Long,
    val rating: Int = 0,
)

fun ChannelModel.toEntity() = ChannelEntity(
    this.id,
    this.unreadCount,
    this.lastReadMessageId,
    this.rating,
)