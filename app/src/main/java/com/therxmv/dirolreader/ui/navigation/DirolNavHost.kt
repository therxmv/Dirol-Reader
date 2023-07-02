package com.therxmv.dirolreader.ui.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.therxmv.dirolreader.ui.auth.AuthScreen
import com.therxmv.dirolreader.ui.news.NewsScreen
import com.therxmv.dirolreader.ui.profile.ProfileScreen
import com.therxmv.dirolreader.ui.settings.StorageScreen
import com.therxmv.dirolreader.ui.settings.ThemingScreen

@Composable
fun DirolNavHost(
    switchDynamicTheme: (Boolean) -> Unit
) {
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
            NewsScreen(
                onNavigateToProfile = {
                    navController.navigate(Destination.ProfileScreen.route) {
                        popUpTo(Destination.NewsScreen.route) { inclusive = false }
                    }
                }
            )
        }
        composable(route = Destination.ProfileScreen.route) {
            ProfileScreen(
                navController,
                onNavigateToTheming = {
                    navController.navigate(Destination.SettingsThemingScreen.route) {
                        popUpTo(Destination.ProfileScreen.route) { inclusive = false }
                    }
                },
                onNavigateToStorage = {
                    navController.navigate(Destination.SettingsStorageScreen.route) {
                        popUpTo(Destination.ProfileScreen.route) { inclusive = false }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(Destination.AuthScreen.route) {
                        popUpTo(Destination.ProfileScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = Destination.SettingsThemingScreen.route) {
            ThemingScreen(
                navController,
                switchDynamicTheme
            )
        }
        composable(route = Destination.SettingsStorageScreen.route) {
            StorageScreen(
                navController,
            )
        }
    }
}