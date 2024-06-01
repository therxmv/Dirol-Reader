package com.therxmv.otaupdates.presentation.view

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.therxmv.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OtaUpdateContent(
    onNavigateToAuth: () -> Unit,
    updateVersion: String,
    changeLog: String,
    updateButtonTitle: Int,
    isButtonEnabled: Boolean,
    onUpdateClick: () -> Unit,
) {
    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(10.dp),
            contentAlignment = Alignment.Center,
        ) {
            UpdateLaterButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    onNavigateToAuth()
                },
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 42.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier.size(172.dp),
                    painter = painterResource(id = R.drawable.logo_icon),
                    contentDescription = "Logo",
                )
                NewVersionTitle()
                Spacer(modifier = Modifier.height(24.dp))
                NewVersionSubtitle(updateVersion = updateVersion)
                Spacer(modifier = Modifier.height(14.dp))

                ChangeLogText(changeLog)
                Spacer(modifier = Modifier.height(24.dp))

                DownloadButton(
                    text = updateButtonTitle,
                    onClick = onUpdateClick,
                    isEnabled = isButtonEnabled,
                )
            }
        }
    }
}

@Composable
private fun UpdateLaterButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(text = stringResource(id = R.string.ota_update_later))
    }
}

@Composable
private fun DownloadButton(
    @StringRes text: Int,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
    ) {
        Text(
            text = stringResource(id = text)
        )
    }
}

@Composable
private fun ChangeLogText(
    changeLog: String,
) {
    Text(
        text = changeLog,
        style = MaterialTheme.typography.bodyMedium,
        textAlign = TextAlign.Center,
    )
}

@Composable
fun NewVersionTitle() {
    Text(
        text = stringResource(id = R.string.ota_new_update),
        style = MaterialTheme.typography.headlineMedium,
        fontWeight = FontWeight.Bold,
    )
}

@Composable
fun NewVersionSubtitle(
    updateVersion: String,
) {
    Text(
        text = "${stringResource(id = R.string.app_name)} $updateVersion",
        style = MaterialTheme.typography.titleMedium,
    )
}

@Preview(showBackground = true)
@Composable
private fun NewVersionTitlePreview() {
    NewVersionTitle()
}

@Preview(showBackground = true)
@Composable
private fun NewVersionSubtitlePreview() {
    NewVersionSubtitle("1.0.0")
}

@Preview(showBackground = true)
@Composable
private fun ChangeLogTextPreview() {
    ChangeLogText("Changelog text Changelog text Changelog text Changelog text Changelog text Changelog text")
}

@Preview(showBackground = true)
@Composable
private fun DownloadButtonPreview() {
    DownloadButton(
        text = R.string.ota_download_now,
        onClick = {},
        isEnabled = true,
    )
}

@Preview(showBackground = true)
@Composable
private fun DisabledDownloadButtonPreview() {
    DownloadButton(
        text = R.string.ota_download_now,
        onClick = {},
        isEnabled = false,
    )
}

@Preview(showBackground = true)
@Composable
private fun UpdateLaterButtonPreview() {
    UpdateLaterButton(onClick = {})
}