package com.therxmv.otaupdates.downloadmanager

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.therxmv.otaupdates.data.models.LatestReleaseModel
import javax.inject.Inject

class LatestReleaseDownloader @Inject constructor(
    private val context: Context,
): Downloader {
    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(latestReleaseModel: LatestReleaseModel): Long {
        val request = DownloadManager.Request(latestReleaseModel.assets.first().downloadUrl.toUri())
            .setMimeType(latestReleaseModel.assets.first().contentType)
            .setTitle(latestReleaseModel.assets.first().fileName.replace(".apk", ""))
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, latestReleaseModel.assets.first().fileName)

        return downloadManager.enqueue(request)
    }
}