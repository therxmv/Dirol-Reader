package com.therxmv.dirolreader.data.source.remote.media

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MediaRemoteDataSource @Inject constructor(
    private val client: Client,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
) : MediaSource {

    override suspend fun downloadMediaAndGetPath(mediaId: Int): String = withContext(ioDispatcher) {
        suspendCoroutine {
            client.send(
                TdApi.DownloadFile(
                    /* fileId = */ mediaId,
                    /* priority = */ 32,
                    /* offset = */ 0,
                    /* limit = */ 0,
                    /* synchronous = */ true,
                )
            ) { file ->
                file as TdApi.File
                it.resume(file.local.path)
            }
        }
    }
}