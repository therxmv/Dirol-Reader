package com.therxmv.dirolreader.ui.ota.utils

import android.content.Context

sealed class OtaUiEvent {
    object DownloadUpdate : OtaUiEvent()
    data class InstallUpdate(val context: Context) : OtaUiEvent()
}