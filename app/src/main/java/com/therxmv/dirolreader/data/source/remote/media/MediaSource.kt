package com.therxmv.dirolreader.data.source.remote.media

interface MediaSource {

    suspend fun downloadMediaAndGetPath(mediaId: Int): String
}