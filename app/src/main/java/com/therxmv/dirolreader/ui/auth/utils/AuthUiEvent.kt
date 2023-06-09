package com.therxmv.dirolreader.ui.auth.utils

sealed class AuthUiEvent {
    data class ConfirmInput(val authState: AuthState, val input: String): AuthUiEvent()
}
