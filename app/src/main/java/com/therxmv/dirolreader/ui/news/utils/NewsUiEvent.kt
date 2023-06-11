package com.therxmv.dirolreader.ui.news.utils

sealed class NewsUiEvent {
    data class UpdateRating(val id: Long, val num: Int): NewsUiEvent()
}
