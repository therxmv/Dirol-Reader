package com.therxmv.dirolreader.ui.auth

import android.app.Activity
import android.content.Context
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.therxmv.common.R
import com.therxmv.dirolreader.ui.auth.utils.AuthState
import com.therxmv.dirolreader.ui.auth.utils.AuthUiEvent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    onNavigateToNews: () -> Unit,
    viewModel: AuthViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    var inputValue by remember { mutableStateOf("") }
    var isDialogOpened by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val surfaceColor = MaterialTheme.colorScheme.surface.toArgb()

    if (state.authState == AuthState.READY) {
        LaunchedEffect(Unit) {
            onNavigateToNews()
        }
    }

    Scaffold { padding ->
        if (state.authState != AuthState.START
            && state.authState != AuthState.READY
        ) {
            SwitchImmersive(context, false, surfaceColor)
            if (isDialogOpened) {
                AlertDialog(
                    shape = MaterialTheme.shapes.medium,
                    onDismissRequest = { isDialogOpened = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                isDialogOpened = false
                                viewModel.onEvent(AuthUiEvent.ConfirmInput(state.authState, inputValue))
                                inputValue = ""
                            }
                        ) {
                            Text(text = stringResource(id = R.string.auth_dialog_confirm))
                        }
                    },
                    title = {
                        Text(text = stringResource(id = R.string.auth_dialog_title))
                    },
                    text = {
                        Text(text = "${stringResource(id = R.string.auth_dialog_text)} $inputValue")
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(30.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(172.dp)
                        .clip(MaterialTheme.shapes.extraLarge),
                    painter = painterResource(id = R.drawable.logo_icon),
                    contentDescription = "Logo",
                )
                Text(
                    text = getTitleString(LocalContext.current, state.authState),
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                )
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    value = inputValue,
                    onValueChange = {
                        inputValue = it
                        viewModel.disableError()
                    },
                    keyboardOptions = getKeyboardOptions(state.authState),
                    visualTransformation = if (state.authState == AuthState.PASSWORD) PasswordVisualTransformation() else VisualTransformation.None,
                    isError = !state.isValidInput,
                    supportingText = {
                        if (!state.isValidInput) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = stringResource(id = R.string.auth_incorrect_input),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    trailingIcon = {
                        if (!state.isValidInput) {
                            Icon(
                                painterResource(id = R.drawable.error_icon),
                                "error",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                )
                Button(
                    modifier = Modifier
                        .align(Alignment.End),
                    onClick = {
                        if (state.authState == AuthState.PHONE) {
                            isDialogOpened = true
                        } else {
                            viewModel.onEvent(AuthUiEvent.ConfirmInput(state.authState, inputValue))
                            inputValue = ""
                        }
                    },
                ) {
                    Text(text = stringResource(id = R.string.auth_continue_btn))
                }
            }
        } else {
            SwitchImmersive(context, true, surfaceColor)
        }
    }
}

private fun getKeyboardOptions(state: AuthState): KeyboardOptions {
    return when (state) {
        AuthState.PHONE -> KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        )

        AuthState.CODE -> KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Done
        )

        AuthState.PASSWORD -> KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        )

        else -> KeyboardOptions.Default
    }
}

@Composable
private fun SwitchImmersive(context: Context, isEnabled: Boolean, surfaceColor: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val color = if (!isEnabled) surfaceColor else Color.Transparent.toArgb()

        SideEffect {
            val window = (context as Activity).window
            window.statusBarColor = color
            window.navigationBarColor = color
            window.isStatusBarContrastEnforced = false
            window.isNavigationBarContrastEnforced = false

            WindowCompat.setDecorFitsSystemWindows(window, !isEnabled)
        }
    }
}

private fun getTitleString(context: Context, state: AuthState): String {
    return when (state) {
        AuthState.PHONE -> context.getString(R.string.auth_title_phone)
        AuthState.CODE -> context.getString(R.string.auth_title_code)
        AuthState.PASSWORD -> context.getString(R.string.auth_title_password)
        AuthState.ERROR -> context.getString(R.string.auth_title_error)
        else -> ""
    }
}