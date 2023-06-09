package com.therxmv.dirolreader.ui.navigation

sealed class Destination(val route: String) {
    object AuthScreen: Destination("authScreen")
    object NewsScreen: Destination("newsScreen")
}
