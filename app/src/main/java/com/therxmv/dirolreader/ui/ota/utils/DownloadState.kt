package com.therxmv.dirolreader.ui.ota.utils

enum class DownloadState {
    DOWNLOAD,
    DOWNLOADING,
    DOWNLOADED,
}

fun Boolean.toDownloadState() = if (this) DownloadState.DOWNLOADED else DownloadState.DOWNLOAD