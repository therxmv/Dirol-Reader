package com.therxmv.dirolreader.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.therxmv.dirolreader.ui.MainNavigationActivity
import com.therxmv.dirolreader.ui.MainNavigationActivity.Companion.INITIAL_ROUTE_EXTRA
import com.therxmv.dirolreader.ui.splash.SplashViewModel.Event.NavigateToNextScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity: ComponentActivity() {

    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        setContent {
            val event by viewModel.eventFlow.collectAsState()

            // will be navigated to the navigation activity once ready
            splashScreen.setKeepOnScreenCondition { true }

            LaunchedEffect(event) {
                if (event == null) return@LaunchedEffect

                if (event is NavigateToNextScreen) {
                    val intent = Intent(this@MainActivity, MainNavigationActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        putExtra(INITIAL_ROUTE_EXTRA, (event as NavigateToNextScreen).route)
                    }
                    this@MainActivity.startActivity(intent)
                }
            }
        }
    }
}