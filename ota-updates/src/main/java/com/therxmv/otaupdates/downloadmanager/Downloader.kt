package com.therxmv.otaupdates.downloadmanager

import com.therxmv.otaupdates.data.models.LatestReleaseModel

interface Downloader {
    fun downloadFile(latestReleaseModel: LatestReleaseModel): Long
}