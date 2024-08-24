package com.therxmv.dirolreader.ui.profile.view

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.therxmv.common.R
import com.therxmv.dirolreader.ui.commonview.CenteredBoxLoader
import com.therxmv.dirolreader.ui.commonview.FillOptions

@Composable
fun SignOutDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    var isSigningOut by remember { mutableStateOf(false) }

    if (isSigningOut) {
        SignOutInProgressDialog()
    } else {
        SignOutMainDialog(
            onClick = {
                isSigningOut = true
                onClick()
            },
            onDismiss = onDismiss,
        )
    }
}

@Composable
private fun SignOutInProgressDialog() {
    AlertDialog(
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = {},
        confirmButton = {},
        title = {
            Text(text = stringResource(id = R.string.sign_out_dialog_title_in_progress))
        },
        text = {
            CenteredBoxLoader(option = FillOptions.WIDTH)
        }
    )
}

@Composable
private fun SignOutMainDialog(
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        shape = MaterialTheme.shapes.medium,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onClick,
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
    SignOutMainDialog(
        onClick = {},
        onDismiss = {},
    )
}

@Preview
@Composable
private fun SignOutInProgressPreview() {
    SignOutInProgressDialog()
}