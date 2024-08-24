package com.therxmv.dirolreader.ui.auth.view

import androidx.compose.animation.Crossfade
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.therxmv.dirolreader.ui.auth.viewmodel.AuthViewModel
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthState
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthUiEvent
import com.therxmv.dirolreader.ui.commonview.CenteredBoxLoader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onNavigateToNews: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val authState by viewModel.authState.collectAsState()
    val inputState by viewModel.inputState.collectAsState()

    Scaffold { padding ->
        if (authState != AuthState.START
            && authState != AuthState.READY
        ) {
            Crossfade(
                targetState = authState,
                label = "content",
            ) {
                when (it) {
                    AuthState.PROCESSING -> CenteredBoxLoader()

                    else -> AuthScreenContent(
                        authState = authState,
                        inputState = inputState,
                        screenPadding = padding,
                        confirmInput = {
                            viewModel.onEvent(AuthUiEvent.ConfirmInput)
                        },
                        onValueChange = { input ->
                            viewModel.onEvent(AuthUiEvent.OnValueChange(input))
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(authState) {
        if (authState == AuthState.READY) onNavigateToNews()
    }
}