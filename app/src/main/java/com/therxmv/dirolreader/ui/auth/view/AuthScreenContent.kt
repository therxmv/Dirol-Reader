package com.therxmv.dirolreader.ui.auth.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.therxmv.common.R
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthInputState
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.AuthState
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.getKeyboardOptions
import com.therxmv.dirolreader.ui.auth.viewmodel.utils.getTitleString

@Composable
fun AuthScreenContent(
    authState: AuthState,
    inputState: AuthInputState,
    screenPadding: PaddingValues,
    confirmInput: () -> Unit,
    onValueChange: (String) -> Unit,
) {
    val context = LocalContext.current
    var isDialogOpened by remember { mutableStateOf(false) }

    if (isDialogOpened) {
        ConfirmPhoneDialog(
            phoneNumber = inputState.inputValue,
            onDismiss = { isDialogOpened = false },
            onConfirm = confirmInput,
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(screenPadding)
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
        Spacer(modifier = Modifier.height(32.dp))

        Title(
            text = authState.getTitleString(context),
        )
        Spacer(modifier = Modifier.height(16.dp))

        InputField(
            authState = authState,
            value = inputState.inputValue,
            isValidInput = inputState.isValidInput,
            onValueChange = onValueChange,
        )
        Spacer(modifier = Modifier.height(16.dp))

        ConfirmButton(
            modifier = Modifier
                .align(Alignment.End),
            onClick = {
                if (authState == AuthState.PHONE) {
                    isDialogOpened = true
                } else {
                    confirmInput()
                }
            },
        )
    }
}
@Composable
private fun Title(
    text: String,
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
private fun ConfirmButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(text = stringResource(id = R.string.auth_continue_btn))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InputField(
    authState: AuthState,
    value: String,
    isValidInput: Boolean,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = authState.getKeyboardOptions(),
        visualTransformation = if (authState == AuthState.PASSWORD) PasswordVisualTransformation() else VisualTransformation.None,
        isError = isValidInput.not(),
        supportingText = {
            if (isValidInput.not()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = R.string.auth_incorrect_input),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        trailingIcon = {
            if (isValidInput.not()) {
                Icon(
                    painterResource(id = R.drawable.error_icon),
                    "error",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
private fun TitlePreview() {
    Title(text = "Title")
}

@Preview(showBackground = true)
@Composable
private fun ConfirmButtonPreview() {
    ConfirmButton {}
}

@Preview(showBackground = true)
@Composable
private fun InputFieldPreview() {
    InputField(
        authState = AuthState.PHONE,
        value = "value",
        isValidInput = true,
        onValueChange = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun PasswordInputFieldPreview() {
    InputField(
        authState = AuthState.PASSWORD,
        value = "value",
        isValidInput = true,
        onValueChange = {},
    )
}

@Preview(showBackground = true)
@Composable
private fun ErrorInputFieldPreview() {
    InputField(
        authState = AuthState.PHONE,
        value = "value",
        isValidInput = false,
        onValueChange = {},
    )
}