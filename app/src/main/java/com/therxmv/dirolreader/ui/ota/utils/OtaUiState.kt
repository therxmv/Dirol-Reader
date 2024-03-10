package com.therxmv.dirolreader.ui.ota.utils

sealed class OtaUiState {
    object InitialState : OtaUiState()
    object NoUpdates : OtaUiState()
    object DownloadUpdate : OtaUiState()
    object Downloading : OtaUiState()
    object Downloaded : OtaUiState()
}

fun Boolean.toDownloadState() = if (this) OtaUiState.Downloaded else OtaUiState.DownloadUpdate