package com.therxmv.dirolreader.ui.profile.viewmodel.utils

sealed class ProfileUiEvent {
    data class LogOut(val onComplete: () -> Unit) : ProfileUiEvent()
}
