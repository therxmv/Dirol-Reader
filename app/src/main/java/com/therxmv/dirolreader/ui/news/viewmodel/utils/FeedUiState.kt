package com.therxmv.dirolreader.ui.news.viewmodel.utils

sealed class FeedUiState {
    data class Ready(
        val toolbarState: ToolbarState,
    ) : FeedUiState()

    data object InitialState : FeedUiState()
}

data class ToolbarState(
    val avatarPath: String = "",
    val userName: String = "",
    val unreadChannels: Int = 0,
)