package com.therxmv.dirolreader.ui.news.viewmodel.utils

sealed class FeedUiState {
    data class Ready(
        val toolbarState: ToolbarState,
    ) : FeedUiState()

    data object InitialState : FeedUiState()
}