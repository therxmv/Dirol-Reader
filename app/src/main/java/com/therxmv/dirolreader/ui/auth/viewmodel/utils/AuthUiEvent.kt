package com.therxmv.dirolreader.ui.auth.viewmodel.utils

sealed class AuthUiEvent {
    data object ConfirmInput: AuthUiEvent()
    data class OnValueChange(val value: String): AuthUiEvent()
}
