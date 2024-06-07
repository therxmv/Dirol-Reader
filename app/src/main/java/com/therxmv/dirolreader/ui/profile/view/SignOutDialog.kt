package com.therxmv.dirolreader.ui.profile.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.therxmv.common.R

@Composable
fun SignOutDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onClick()
                    onDismiss()
                }
            ) {
                Text(text = stringResource(id = R.string.auth_dialog_confirm))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.sign_out_dialog_title))
        },
        text = {
            Text(text = stringResource(id = R.string.sign_out_dialog_text))
        }
    )
}

@Preview
@Composable
private fun SignOutDialogPreview() {
    SignOutDialog(
        onClick = {},
        onDismiss = {},
    )
}