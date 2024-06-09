package com.therxmv.dirolreader.ui.navigation

sealed class NavArguments(val name: String) {
    object SettingsDestination : NavArguments("destination")
}