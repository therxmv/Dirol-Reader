package com.therxmv.dirolreader.data.source.remote.user

import com.therxmv.dirolreader.data.entity.UserEntity
import com.therxmv.dirolreader.data.source.remote.media.MediaSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.ChatPhoto
import org.drinkless.tdlib.TdApi.User
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRemoteDataSource @Inject constructor(
    private val client: Client,
    private val mediaRemoteDataSource: MediaSource,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
) : UserSource {

    override suspend fun getCurrentUser(): UserEntity =
        withContext(ioDispatcher) {
            val user = getUser()
            val profilePhoto = getLastProfilePhoto(user)

            val avatarPath = profilePhoto?.let { photo ->
                mediaRemoteDataSource.downloadMediaAndGetPath(photo.sizes[1].photo.id)
            }.orEmpty()

            UserEntity(
                firstName = user.firstName,
                lastName = user.lastName,
                avatarPath = avatarPath,
            )
        }

    private suspend fun getUser(): User =
        suspendCoroutine {
            client.send(TdApi.GetMe()) { user ->
                it.resume(user as User)
            }
        }

    private suspend fun getLastProfilePhoto(user: User): ChatPhoto? =
        suspendCoroutine {
            client.send(TdApi.GetUserProfilePhotos(user.id, 0, 1)) { photos ->
                photos as TdApi.ChatPhotos
                it.resume(photos.photos.firstOrNull())
            }
        }
}