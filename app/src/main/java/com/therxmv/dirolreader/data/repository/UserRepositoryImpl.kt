package com.therxmv.dirolreader.data.repository

import com.therxmv.dirolreader.data.source.remote.UserRemoteDataSource
import com.therxmv.dirolreader.domain.repository.UserRepository
import com.therxmv.dirolreader.ui.news.utils.ToolbarState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.User
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
): UserRepository {
    override suspend fun getCurrentUser(client: Client?): User {
        return userRemoteDataSource.getCurrentUser(client)
    }

    override suspend fun getCurrentUserAvatar(client: Client?, user: User): String {
        return userRemoteDataSource.getCurrentUserAvatar(client, user)
    }
}