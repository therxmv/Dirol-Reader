package com.therxmv.otaupdates.presentation.viewmodel.utils

import android.content.Context
import com.therxmv.otaupdates.domain.models.LatestReleaseModel

sealed class OtaUiEvent {
    data class DownloadUpdate(val updateModel: LatestReleaseModel?) : OtaUiEvent()
    data class InstallUpdate(val context: Context, val updateModel: LatestReleaseModel?) : OtaUiEvent()
}