package com.therxmv.dirolreader.ui

import android.app.ActivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.therxmv.dirolreader.ui.news.view.NewsScreen
import com.therxmv.dirolreader.ui.profile.view.ProfileScreen
import com.therxmv.dirolreader.ui.settings.view.SettingsScreen
import com.therxmv.dirolreader.ui.settings.viewmodel.SettingsViewModel
import com.therxmv.dirolreader.ui.theme.AppTheme
import com.therxmv.otaupdates.presentation.view.OtaScreen
import com.therxmv.sharedpreferences.repository.AppSharedPrefsRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainNavigationActivity : ComponentActivity() {

    companion object {
        const val INITIAL_ROUTE_EXTRA = "InitialRoute"
    }

    @Inject
    lateinit var appSharedPrefsRepository: AppSharedPrefsRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val route = intent.getStringExtra(INITIAL_ROUTE_EXTRA) ?: Destination.AuthScreen.route

        setContent {
            val navController = rememberNavController()
            var isDynamic by rememberSaveable { mutableStateOf(appSharedPrefsRepository.isDynamic) }

            AppTheme(
                dynamicColor = isDynamic
            ) {
                NavHost(navController = navController, startDestination = route) {
                    composable(route = Destination.OtaScreen.route) {
                        val nextScreen = route.split("/").getOrNull(1) // for some reason arguments are null
                            ?: Destination.AuthScreen.route
                        OtaScreen(
                            onNavigateToNextScreen = {
                                navController.navigate(nextScreen) {
                                    popUpTo(Destination.OtaScreen.route) { inclusive = true }
                                    launchSingleTop = true
                                }
                            }
                        )
                        BackHandler {
                            finishAffinity()
                        }
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
                        BackHandler {
                            finishAffinity()
                        }
                    }
                    composable(route = Destination.NewsScreen.route) {
                        NewsScreen(
                            navController = navController,
                            onNavigateToProfile = {
                                navController.navigate(Destination.ProfileScreen.route) {
                                    popUpTo(Destination.NewsScreen.route) { inclusive = false }
                                }
                            },
                        )
                        BackHandler {
                            finishAffinity()
                        }
                    }
                    composable(route = Destination.ProfileScreen.route) {
                        ProfileScreen(
                            navController = navController,
                            onNavigateToRoute = {
                                navController.navigate(it) {
                                    popUpTo(Destination.ProfileScreen.route) { inclusive = false }
                                }
                            },
                            eraseApplication = {
                                val activityManager = getSystemService(ActivityManager::class.java)
                                activityManager.clearApplicationUserData()
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