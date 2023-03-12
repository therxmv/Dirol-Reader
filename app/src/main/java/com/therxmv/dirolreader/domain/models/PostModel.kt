package com.therxmv.dirolreader.domain.models

data class PostModel (
    val id: Long,
    val messageThreadId: Long,
    val channelId: Long,
    val date: Int,
    val text: String,
    val photoPath: String?,
    val isViewed: Boolean,
    val isLast: Boolean,
    val title: String,
    val avatarPath: String?,
)