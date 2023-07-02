package com.therxmv.dirolreader.ui.profile.utils

data class ProfileUiState(
    val appBarState: AppBarState = AppBarState(),
)

data class AppBarState(
    val avatarPath: String = "",
    val userName: String = "",
)