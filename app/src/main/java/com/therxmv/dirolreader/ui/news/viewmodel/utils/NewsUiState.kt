package com.therxmv.dirolreader.ui.news.viewmodel.utils

data class NewsUiState(
    val toolbarState: ToolbarState = ToolbarState(),
    val isLoaded: Boolean = false,
)

data class ToolbarState(
    val avatarPath: String = "",
    val userName: String = "",
    val unreadChannels: Int = 0,
)