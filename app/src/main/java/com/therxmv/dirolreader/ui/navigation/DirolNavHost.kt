package com.therxmv.dirolreader.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.therxmv.dirolreader.ui.auth.view.AuthScreen
import com.therxmv.dirolreader.ui.navigation.Destination.AuthScreen
import com.therxmv.dirolreader.ui.navigation.Destination.NewsScreen
import com.therxmv.dirolreader.ui.navigation.Destination.OtaScreen
import com.therxmv.dirolreader.ui.navigation.Destination.ProfileScreen
import com.therxmv.dirolreader.ui.navigation.Destination.SettingsStorageScreen
import com.therxmv.dirolreader.ui.navigation.Destination.SettingsThemingScreen
import com.therxmv.dirolreader.ui.news.NewsScreen
import com.therxmv.dirolreader.ui.profile.ProfileScreen
import com.therxmv.dirolreader.ui.settings.StorageScreen
import com.therxmv.dirolreader.ui.settings.ThemingScreen
import com.therxmv.otaupdates.presentation.view.OtaScreen

@Composable
fun DirolNavHost(
    switchDynamicTheme: (Boolean) -> Unit,
) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = OtaScreen.route) {
        composable(route = OtaScreen.route) {
            OtaScreen(
                onNavigateToAuth = {
                    navController.navigate(AuthScreen.route) {
                        popUpTo(OtaScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = AuthScreen.route) {
            AuthScreen(
                onNavigateToNews = {
                    navController.navigate(NewsScreen.route) {
                        popUpTo(AuthScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = NewsScreen.route) {
            NewsScreen(
                onNavigateToProfile = {
                    navController.navigate(ProfileScreen.route) {
                        popUpTo(NewsScreen.route) { inclusive = false }
                    }
                }
            )
        }
        composable(route = ProfileScreen.route) {
            ProfileScreen(
                navController,
                onNavigateToTheming = {
                    navController.navigate(SettingsThemingScreen.route) {
                        popUpTo(ProfileScreen.route) { inclusive = false }
                    }
                },
                onNavigateToStorage = {
                    navController.navigate(SettingsStorageScreen.route) {
                        popUpTo(ProfileScreen.route) { inclusive = false }
                    }
                },
                onNavigateToAuth = {
                    navController.navigate(AuthScreen.route) {
                        popUpTo(ProfileScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable(route = SettingsThemingScreen.route) {
            ThemingScreen(
                navController,
                switchDynamicTheme
            )
        }
        composable(route = SettingsStorageScreen.route) {
            StorageScreen(
                navController,
            )
        }
    }
}