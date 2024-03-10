package com.therxmv.dirolreader.data.source.remote.user

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRemoteDataSource @Inject constructor() : UserSource {

    override suspend fun getCurrentUser(client: Client?): User =
        withContext(Dispatchers.IO) {
            suspendCoroutine {
                client?.send(TdApi.GetMe()) { u ->
                    it.resume(u as User)
                }
            }
        }

    override suspend fun getCurrentUserAvatar(client: Client?, user: User): String =
        withContext(Dispatchers.IO) {
            suspendCoroutine {
                client?.send(TdApi.GetUserProfilePhotos(user.id, 0, 1)) { p ->
                    p as TdApi.ChatPhotos

                    client.send(
                        TdApi.DownloadFile(
                            p.photos.first().sizes[1].photo.id,
                            32,
                            0,
                            1,
                            true
                        )
                    ) { f ->
                        f as TdApi.File
                        it.resume(f.local.path)
                    }
                }
            }
        }
}