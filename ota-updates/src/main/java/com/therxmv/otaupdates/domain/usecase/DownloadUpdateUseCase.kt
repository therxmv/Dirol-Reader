package com.therxmv.otaupdates.domain.usecase

import com.therxmv.otaupdates.domain.models.LatestReleaseModel
import com.therxmv.otaupdates.downloadmanager.Downloader
import javax.inject.Inject

class DownloadUpdateUseCase @Inject constructor(
    private val downloader: Downloader,
) {
    operator fun invoke(latestReleaseModel: LatestReleaseModel) {
        downloader.downloadFile(latestReleaseModel)
    }
}