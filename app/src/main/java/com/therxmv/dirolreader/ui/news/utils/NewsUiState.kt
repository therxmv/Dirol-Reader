package com.therxmv.dirolreader.ui.news.utils

data class NewsUiState(
    val toolbarState: ToolbarState = ToolbarState()
)

data class ToolbarState(
    val avatarPath: String = "",
    val userName: String = "",
    val unreadChannels: Int = 0
)