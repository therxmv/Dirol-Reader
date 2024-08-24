package com.therxmv.dirolreader.ui.navigation

sealed class Destination(val route: String) {
    data object OtaScreen: Destination("otaScreen/{${NavArguments.OtaNextScreen.name}}") {
        fun createRoute(nextScreen: String) = "otaScreen/$nextScreen"
    }
    data object AuthScreen: Destination("authScreen")
    data object NewsScreen: Destination("newsScreen")
    data object ProfileScreen: Destination("profileScreen")
    data object SettingsScreen: Destination("settingsScreen/{${NavArguments.SettingsDestination.name}}") {
        fun createRoute(destination: String) = "settingsScreen/$destination"
    }
}
