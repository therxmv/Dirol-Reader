package com.therxmv.dirolreader.data.repository

import com.therxmv.dirolreader.data.entity.toDomain
import com.therxmv.dirolreader.data.source.remote.user.UserSource
import com.therxmv.dirolreader.domain.repository.UserRepository
import org.drinkless.td.libcore.telegram.Client
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userRemoteDataSource: UserSource,
) : UserRepository {

    override suspend fun getCurrentUser(client: Client?) =
        userRemoteDataSource.getCurrentUser(client).toDomain()
}