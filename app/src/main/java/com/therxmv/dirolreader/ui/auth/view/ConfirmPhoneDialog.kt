package com.therxmv.dirolreader.ui.auth.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.therxmv.common.R

@Composable
fun ConfirmPhoneDialog(
    phoneNumber: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onConfirm()
                }
            ) {
                Text(text = stringResource(id = R.string.auth_dialog_confirm))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.auth_dialog_title))
        },
        text = {
            Text(
                text = "${stringResource(id = R.string.auth_dialog_text)} $phoneNumber"
            )
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun ConfirmPhoneDialogPreview() {
    ConfirmPhoneDialog(
        phoneNumber = "+380634567890",
        onDismiss = {},
        onConfirm = {},
    )
}