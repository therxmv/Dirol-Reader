package com.therxmv.dirolreader.data.source.remote.user

import com.therxmv.dirolreader.data.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.User
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRemoteDataSource @Inject constructor() : UserSource {

    override suspend fun getCurrentUser(client: Client?): UserEntity =
        withContext(Dispatchers.IO) {
            val user = getUser(client)

            suspendCoroutine {
                client?.send(TdApi.GetUserProfilePhotos(user.id, 0, 1)) { photos ->
                    photos as TdApi.ChatPhotos

                    client.send(
                        TdApi.DownloadFile(
                            /* fileId = */ photos.photos.first().sizes[1].photo.id,
                            /* priority = */ 32,
                            /* offset = */ 0,
                            /* limit = */ 1,
                            /* synchronous = */ true
                        )
                    ) { file ->
                        file as TdApi.File

                        it.resume(
                            UserEntity(
                                firstName = user.firstName,
                                lastName = user.lastName,
                                avatarPath = file.local.path,
                            )
                        )
                    }
                }
            }
        }

    private suspend fun getUser(client: Client?): User =
        withContext(Dispatchers.IO) {
            suspendCoroutine {
                client?.send(TdApi.GetMe()) { user ->
                    it.resume(user as User)
                }
            }
        }
}