package com.therxmv.otaupdates.presentation.view

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.therxmv.common.R
import com.therxmv.otaupdates.presentation.viewmodel.OtaViewModel
import com.therxmv.otaupdates.presentation.viewmodel.utils.OtaUiEvent
import com.therxmv.otaupdates.presentation.viewmodel.utils.OtaUiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun OtaScreen(
    onNavigateToNextScreen: () -> Unit,
    viewModel: OtaViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    val writeStoragePermissionState = rememberPermissionState(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    when (uiState) {
        is OtaUiState.InitialState -> {}

        else -> {
            val update = uiState.updateModel

            OtaUpdateContent(
                onNavigateToAuth = onNavigateToNextScreen,
                updateVersion = update?.version.orEmpty(),
                changeLog = update?.changeLog.orEmpty(),
                updateButtonTitle = uiState.getDownloadTitle(),
                isButtonEnabled = uiState !is OtaUiState.Downloading,
                onUpdateClick = {
                    when (uiState) {
                        is OtaUiState.DownloadUpdate -> {
                            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P || writeStoragePermissionState.status.isGranted) {
                                viewModel.onEvent(OtaUiEvent.DownloadUpdate(update))
                            } else {
                                writeStoragePermissionState.launchPermissionRequest()
                            }
                        }

                        is OtaUiState.Downloaded -> {
                            viewModel.onEvent(OtaUiEvent.InstallUpdate(context, update))
                        }

                        else -> {}
                    }
                },
            )
        }
    }
}

private fun OtaUiState.getDownloadTitle() = when (this) {
    is OtaUiState.Downloading -> R.string.ota_downloading
    is OtaUiState.Downloaded -> R.string.ota_install_update
    else -> R.string.ota_download_now
}