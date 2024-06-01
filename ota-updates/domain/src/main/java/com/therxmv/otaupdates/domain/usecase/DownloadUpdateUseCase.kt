package com.therxmv.otaupdates.domain.usecase

import com.therxmv.otaupdates.domain.models.LatestReleaseModel
import com.therxmv.otaupdates.domain.repository.DownloaderApi
import javax.inject.Inject

class DownloadUpdateUseCase @Inject constructor(
    private val downloaderApi: DownloaderApi,
) {
    operator fun invoke(latestReleaseModel: LatestReleaseModel) {
        downloaderApi.downloadFile(latestReleaseModel)
    }
}