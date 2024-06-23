package com.therxmv.dirolreader.ui.news.viewmodel.utils

sealed class NewsUiState {
    data object Loading : NewsUiState()
    data object Ready : NewsUiState()
}

data class ToolbarData(
    val avatarPath: String = "",
    val userName: String = "",
    val unreadChannels: Int = 0,
)