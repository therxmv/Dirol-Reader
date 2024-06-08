package com.therxmv.dirolreader.ui.navigation

sealed class Destination(val route: String) {
    object OtaScreen: Destination("otaScreen")
    object AuthScreen: Destination("authScreen")
    object NewsScreen: Destination("newsScreen")
    object ProfileScreen: Destination("profileScreen")
    object SettingsThemingScreen: Destination("SettingsThemingScreen") // TODO make arguments
    object SettingsStorageScreen: Destination("SettingsStorageScreen")
}
