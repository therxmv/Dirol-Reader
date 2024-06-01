package com.therxmv.otaupdates.domain.repository

import com.therxmv.otaupdates.domain.models.LatestReleaseModel

interface DownloaderApi {
    fun downloadFile(latestReleaseModel: LatestReleaseModel): Long
}