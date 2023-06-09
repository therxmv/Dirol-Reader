package com.therxmv.dirolreader.data.source.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRemoteDataSource {
    suspend fun getCurrentUser(client: Client?): TdApi.User {
        return withContext(Dispatchers.IO) {
            suspendCoroutine {
                client?.send(TdApi.GetMe()) { u ->
                    it.resume(u as TdApi.User)
                }
            }
        }
    }

    suspend fun getCurrentUserAvatar(client: Client?, user: TdApi.User): String {
        return withContext(Dispatchers.IO) {
            suspendCoroutine {
                client?.send(TdApi.GetUserProfilePhotos(user.id, 0, 1)) { p ->
                    p as TdApi.ChatPhotos
                    client.send(TdApi.DownloadFile(p.photos.first().sizes[1].photo.id, 32, 0, 1, true)) { f ->
                        f as TdApi.File
                        it.resume(f.local.path)
                    }
                }
            }
        }
    }
}