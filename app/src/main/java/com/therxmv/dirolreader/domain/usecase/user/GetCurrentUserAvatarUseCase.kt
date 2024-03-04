package com.therxmv.dirolreader.domain.usecase.user

import com.therxmv.dirolreader.domain.repository.UserRepository
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi.User

class GetCurrentUserAvatarUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(client: Client?, user: User) =
        userRepository.getCurrentUserAvatar(client, user)
}