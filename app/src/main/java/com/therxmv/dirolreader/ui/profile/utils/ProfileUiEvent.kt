package com.therxmv.dirolreader.ui.profile.utils

sealed class ProfileUiEvent {
    data class LogOut(val onComplete: () -> Unit) : ProfileUiEvent()
}
