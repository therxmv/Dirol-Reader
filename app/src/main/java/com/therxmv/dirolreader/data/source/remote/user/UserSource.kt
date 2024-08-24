package com.therxmv.dirolreader.data.source.remote.user

import com.therxmv.dirolreader.data.entity.UserEntity

interface UserSource {
    suspend fun getCurrentUser(): UserEntity
}