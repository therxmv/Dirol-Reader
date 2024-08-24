package com.therxmv.otaupdates.presentation.viewmodel.utils

import com.therxmv.otaupdates.domain.models.LatestReleaseModel

sealed class OtaUiState(open val updateModel: LatestReleaseModel? = null) {
    data object InitialState : OtaUiState()
    data class DownloadUpdate(override val updateModel: LatestReleaseModel) : OtaUiState(updateModel)
    data class Downloading(override val updateModel: LatestReleaseModel) : OtaUiState(updateModel)
    data class Downloaded(override val updateModel: LatestReleaseModel) : OtaUiState(updateModel)
}

fun Boolean.toDownloadState(updateModel: LatestReleaseModel): OtaUiState = if (this) {
    OtaUiState.Downloaded(updateModel)
} else {
    OtaUiState.DownloadUpdate(updateModel)
}