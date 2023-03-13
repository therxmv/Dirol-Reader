package com.therxmv.dirolreader.domain.models

data class PostModel (
    val id: Long,
    val messageThreadId: Long,
    val channelId: Long,
    val date: Int,
    val text: String,
    val photoPath: String?,
    val isLast: Boolean,
    val isNew: Boolean,
    val title: String,
    val avatarId: Int?,
)