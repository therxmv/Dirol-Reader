package com.therxmv.dirolreader.data.source.remote.user

import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

interface UserSource {
    suspend fun getCurrentUser(client: Client?): TdApi.User
    suspend fun getCurrentUserAvatar(client: Client?, user: TdApi.User): String
}