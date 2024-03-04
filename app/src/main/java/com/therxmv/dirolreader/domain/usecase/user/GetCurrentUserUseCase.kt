package com.therxmv.dirolreader.domain.usecase.user

import com.therxmv.dirolreader.domain.repository.UserRepository
import org.drinkless.td.libcore.telegram.Client

class GetCurrentUserUseCase(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(client: Client?) =
        userRepository.getCurrentUser(client)
}