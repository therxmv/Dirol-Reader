package com.therxmv.dirolreader.ui.ota.view

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.therxmv.dirolreader.R
import com.therxmv.dirolreader.ui.ota.OtaViewModel
import com.therxmv.dirolreader.ui.ota.utils.OtaUiEvent
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState.DownloadUpdate
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState.Downloaded
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState.Downloading
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState.InitialState
import com.therxmv.dirolreader.ui.ota.utils.OtaUiState.NoUpdates

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OtaScreen(
    onNavigateToAuth: () -> Unit,
    viewModel: OtaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val writeStoragePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    when (uiState) {
        is NoUpdates -> {
            LaunchedEffect(Unit) {
                onNavigateToAuth()
            }
        }

        is InitialState -> {}

        else -> OtaUpdateContent(
            onNavigateToAuth = onNavigateToAuth,
            updateVersion = viewModel.updateModel?.version.orEmpty(),
            changeLog = viewModel.updateModel?.changeLog.orEmpty(),
            updateButtonTitle = uiState.getDownloadTitle(),
            isButtonEnabled = uiState !is Downloading,
        ) {
            when (uiState) {
                is DownloadUpdate -> {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P
                        || writeStoragePermissionState.status.isGranted
                    ) {
                        viewModel.onEvent(OtaUiEvent.DownloadUpdate)
                    } else {
                        writeStoragePermissionState.launchPermissionRequest()
                    }
                }

                is Downloaded -> {
                    viewModel.onEvent(OtaUiEvent.InstallUpdate(context))
                }

                else -> {}
            }
        }
    }
}

private fun OtaUiState.getDownloadTitle() = when (this) {
    Downloading -> R.string.ota_downloading
    Downloaded -> R.string.ota_install_update
    else -> R.string.ota_download_now
}