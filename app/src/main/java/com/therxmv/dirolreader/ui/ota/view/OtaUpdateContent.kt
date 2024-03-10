package com.therxmv.dirolreader.ui.ota.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import com.therxmv.dirolreader.R

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
            TextButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    onNavigateToAuth()
                },
            ) {
                Text(text = stringResource(id = R.string.ota_update_later))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(172.dp),
                    painter = painterResource(id = R.drawable.logo_icon),
                    contentDescription = "Logo",
                )
                Text(
                    modifier = Modifier.padding(bottom = 24.dp),
                    text = stringResource(id = R.string.ota_new_update),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${stringResource(id = R.string.app_name)} $updateVersion",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    modifier = Modifier
                        .padding(horizontal = 42.dp)
                        .padding(top = 14.dp, bottom = 24.dp),
                    text = changeLog,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                )
                Button(
                    onClick = onUpdateClick,
                    enabled = isButtonEnabled,
                ) {
                    Text(
                        text = stringResource(id = updateButtonTitle)
                    )
                }
            }
        }
    }
}