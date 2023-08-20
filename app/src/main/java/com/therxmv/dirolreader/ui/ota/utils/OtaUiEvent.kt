package com.therxmv.dirolreader.ui.ota.utils

sealed class OtaUiEvent {
    object DownloadUpdate: OtaUiEvent()
}