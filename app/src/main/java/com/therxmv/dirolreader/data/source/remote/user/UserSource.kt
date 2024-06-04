package com.therxmv.dirolreader.data.source.remote.user

import com.therxmv.dirolreader.data.entity.UserEntity
import org.drinkless.td.libcore.telegram.Client

interface UserSource {
    suspend fun getCurrentUser(client: Client?): UserEntity
}