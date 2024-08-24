package com.therxmv.dirolreader.ui.navigation

sealed class NavArguments(val name: String) {
    data object SettingsDestination : NavArguments("destination")
    data object OtaNextScreen : NavArguments("nextScreen")
}