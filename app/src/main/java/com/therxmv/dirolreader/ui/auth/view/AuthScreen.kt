package com.therxmv.dirolreader.ui.auth.view

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.therxmv.dirolreader.ui.auth.viewmodel.AuthViewModel
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthState
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onNavigateToNews: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val authState by viewModel.authState.collectAsState()
    val inputState by viewModel.inputState.collectAsState()
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()

    if (authState == AuthState.READY) {
        LaunchedEffect(Unit) {
            onNavigateToNews()
        }
    }

    Scaffold { padding ->
        if (authState != AuthState.START
            && authState != AuthState.READY
        ) {
            SwitchImmersiveMode(context, false, surfaceColor)
            AuthScreenContent(
                authState = authState,
                inputState = inputState,
                screenPadding = padding,
                confirmInput = {
                    viewModel.onEvent(AuthUiEvent.ConfirmInput)
                },
                onValueChange = {
                    viewModel.onEvent(AuthUiEvent.OnValueChange(it))
                }
            )
        } else {
            SwitchImmersiveMode(context, true, surfaceColor)
        }
    }
}

@Composable
private fun SwitchImmersiveMode(context: Context, isEnabled: Boolean, surfaceColor: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val color = if (isEnabled.not()) surfaceColor else Color.Transparent.toArgb()

        SideEffect {
            (context as? Activity)?.window?.let { window ->
                window.apply {
                    statusBarColor = color
                    navigationBarColor = color
                    isStatusBarContrastEnforced = false
                    isNavigationBarContrastEnforced = false
                }

                WindowCompat.setDecorFitsSystemWindows(window, isEnabled.not())
            }
        }
    }
}