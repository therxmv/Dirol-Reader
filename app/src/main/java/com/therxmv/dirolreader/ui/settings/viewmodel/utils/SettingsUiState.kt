package com.therxmv.dirolreader.ui.settings.viewmodel.utils

sealed class SettingsUiState {
    data class Ready(val data: SettingsContentData) : SettingsUiState()
    object Loading : SettingsUiState()
}