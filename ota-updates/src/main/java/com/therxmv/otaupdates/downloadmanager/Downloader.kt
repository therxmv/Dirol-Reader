package com.therxmv.otaupdates.downloadmanager

import com.therxmv.otaupdates.domain.models.LatestReleaseModel

interface Downloader {
    fun downloadFile(latestReleaseModel: LatestReleaseModel): Long
}