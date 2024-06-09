package com.therxmv.dirolreader.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.therxmv.dirolreader.ui.auth.view.AuthScreen
import com.therxmv.dirolreader.ui.navigation.Destination
import com.therxmv.dirolreader.ui.navigation.NavArguments
import com.therxmv.dirolreader.ui.news.NewsScreen
import com.therxmv.dirolreader.ui.profile.view.ProfileScreen
import com.therxmv.dirolreader.ui.settings.view.SettingsScreen
import com.therxmv.dirolreader.ui.settings.viewmodel.SettingsViewModel
import com.therxmv.dirolreader.ui.theme.AppTheme
import com.therxmv.otaupdates.presentation.view.OtaScreen
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appSharedPrefsRepository: AppSharedPrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            var isDynamic by rememberSaveable { mutableStateOf(appSharedPrefsRepository.isDynamic) }

            AppTheme(
                dynamicColor = isDynamic
            ) {
                Surface {
                    // TODO remove OTA and Auth from chain by checking/creating all necessary data outside
                    //  and navigate straight to the News screen
                    NavHost(navController = navController, startDestination = Destination.OtaScreen.route) {
                        composable(route = Destination.OtaScreen.route) {
                            OtaScreen(
                                onNavigateToAuth = {
                                    navController.navigate(Destination.AuthScreen.route) {
                                        popUpTo(Destination.OtaScreen.route) { inclusive = true }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
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
                                navController = navController,
                                onNavigateToRoute = {
                                    navController.navigate(it) {
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
                        composable(route = Destination.SettingsScreen.route) {
                            val destination = remember { it.arguments?.getString(NavArguments.SettingsDestination.name) }
                            val viewModel = hiltViewModel<SettingsViewModel, SettingsViewModel.Factory>(
                                creationCallback = { factory ->
                                    factory.create(
                                        destination = destination,
                                        toggleDynamicTheme = {
                                            isDynamic = it
                                        }
                                    )
                                }
                            )

                            SettingsScreen(
                                navController = navController,
                                viewModel = viewModel,
                            )
                        }
                    }
                }
            }
        }
    }
}