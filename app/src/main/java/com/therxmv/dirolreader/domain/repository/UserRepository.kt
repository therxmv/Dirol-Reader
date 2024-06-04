package com.therxmv.dirolreader.domain.repository

import com.therxmv.dirolreader.domain.models.UserModel
import org.drinkless.td.libcore.telegram.Client

interface UserRepository {
    suspend fun getCurrentUser(client: Client?): UserModel
}