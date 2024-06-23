package com.therxmv.dirolreader.ui.news.viewmodel.utils

sealed class NewsUiEvent {
    data class UpdateRating(val id: Long, val num: Int): NewsUiEvent()
    data class MarkAsRead(val messageId: Long, val channelId: Long): NewsUiEvent()
}
