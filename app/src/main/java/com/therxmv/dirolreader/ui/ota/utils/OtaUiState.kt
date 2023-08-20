package com.therxmv.dirolreader.ui.ota.utils

import com.therxmv.otaupdates.domain.models.LatestReleaseModel

data class OtaUiState(
    val isUpdateAvailable: Boolean? = null,
    val downloadState: DownloadState = DownloadState.DOWNLOAD,
    val updateModel: LatestReleaseModel? = null,
)