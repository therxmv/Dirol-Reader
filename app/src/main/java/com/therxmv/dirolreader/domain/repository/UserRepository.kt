package com.therxmv.dirolreader.domain.repository

import com.therxmv.dirolreader.domain.models.UserModel

interface UserRepository {
    suspend fun getCurrentUser(): UserModel
}