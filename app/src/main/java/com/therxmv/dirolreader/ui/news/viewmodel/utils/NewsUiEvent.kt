package com.therxmv.dirolreader.ui.news.viewmodel.utils

sealed class NewsUiEvent {
    data class MarkAsRead(val messageId: Long, val channelId: Long) : NewsUiEvent()
    data class Like(val channelId: Long, val isLiked: Boolean?) : NewsUiEvent()
    data class Dislike(val channelId: Long, val isLiked: Boolean?) : NewsUiEvent()
    data class StarChannel(val channelId: Long, val isStarred: Boolean) : NewsUiEvent()
}
