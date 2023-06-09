package com.therxmv.dirolreader.domain.repository

import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi.User

interface UserRepository {
    suspend fun getCurrentUser(client: Client?): User
    suspend fun getCurrentUserAvatar(client: Client?, user: User): String
}