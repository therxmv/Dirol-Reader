package com.therxmv.otaupdates.data.source.remote

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import androidx.core.net.toUri
import com.therxmv.otaupdates.domain.models.LatestReleaseModel
import com.therxmv.otaupdates.domain.repository.DownloaderApi
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LatestReleaseDownloader @Inject constructor(
    @ApplicationContext context: Context,
) : DownloaderApi {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(latestReleaseModel: LatestReleaseModel): Long {
        val request = DownloadManager.Request(latestReleaseModel.downloadUrl.toUri())
            .setMimeType(latestReleaseModel.contentType)
            .setTitle(latestReleaseModel.fileName.replace(".apk", ""))
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, latestReleaseModel.fileName)

        return downloadManager.enqueue(request)
    }
}