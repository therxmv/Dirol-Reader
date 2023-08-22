package com.therxmv.dirolreader.ui.ota

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.therxmv.dirolreader.R
import com.therxmv.dirolreader.ui.ota.utils.DownloadState
import com.therxmv.dirolreader.ui.ota.utils.OtaUiEvent
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState
import java.io.File


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun OtaScreen(
    onNavigateToAuth: () -> Unit,
    viewModel: OtaViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value
    val context = LocalContext.current

    val writeStoragePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    if(state.isUpdateAvailable == false) {
        LaunchedEffect(Unit) {
            onNavigateToAuth()
        }
    }

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(10.dp),
            contentAlignment = Alignment.Center,
        ) {
            if(state.isUpdateAvailable == true && state.updateModel != null) {
                TextButton(
                    modifier = Modifier.align(Alignment.TopEnd),
                    onClick = {
                        onNavigateToAuth()
                    },
                ) {
                    Text(text = stringResource(id = R.string.ota_update_later))
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if(state.isUpdateAvailable == true && state.updateModel != null) {
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
                        text = "${stringResource(id = R.string.app_name)} ${state.updateModel.version}",
                        style = MaterialTheme.typography.titleMedium,
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 42.dp)
                            .padding(top = 14.dp, bottom = 24.dp),
                        text = state.updateModel.changeLog,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                    )
                    Button(
                        onClick = {
                            if(state.downloadState == DownloadState.DOWNLOAD) {
                                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P
                                    || writeStoragePermissionState.status.isGranted
                                ) {
                                    viewModel.onEvent(OtaUiEvent.DownloadUpdate)
                                }
                                else {
                                    writeStoragePermissionState.launchPermissionRequest()
                                }
                            }
                            else if(state.downloadState == DownloadState.DOWNLOADED) {
                                installUpdate(context, state)
                            }
                        },
                        enabled = state.downloadState != DownloadState.DOWNLOADING
                    ) {
                        Text(
                            text = stringResource(id = getDownloadTitle(state.downloadState))
                        )
                    }
                }
            }
        }
    }
}

private fun getDownloadTitle(state: DownloadState) = when(state) {
    DownloadState.DOWNLOAD -> R.string.ota_download_now
    DownloadState.DOWNLOADING -> R.string.ota_downloading
    DownloadState.DOWNLOADED -> R.string.ota_install_update
}

private fun installUpdate(context: Context, state: OtaUiState) {
    val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
    val file = File(path, state.updateModel?.fileName ?: "")

    if(file.exists()) {
        val uri = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", file)
        }
        else {
            Uri.fromFile(file)
        }

        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, state.updateModel?.contentType)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        try {
            context.startActivity(intent)
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
    }
}