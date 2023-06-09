package com.therxmv.dirolreader.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.therxmv.dirolreader.ui.auth.AuthScreen
import com.therxmv.dirolreader.ui.news.NewsScreen

@Composable
fun DirolNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Destination.AuthScreen.route) {
        composable(route = Destination.AuthScreen.route) {
            AuthScreen(
                onNavigateToNews = {
                    navController.navigate(Destination.NewsScreen.route) {
                        popUpTo(Destination.AuthScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = Destination.NewsScreen.route) {
            NewsScreen()
        }
    }
}