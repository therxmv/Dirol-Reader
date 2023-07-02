package com.therxmv.dirolreader.ui.navigation

sealed class Destination(val route: String) {
    object AuthScreen: Destination("authScreen")
    object NewsScreen: Destination("newsScreen")
    object ProfileScreen: Destination("profileScreen")
    object SettingsThemingScreen: Destination("SettingsThemingScreen")
    object SettingsStorageScreen: Destination("SettingsStorageScreen")
}
