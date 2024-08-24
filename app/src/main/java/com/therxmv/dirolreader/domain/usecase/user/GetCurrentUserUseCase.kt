package com.therxmv.dirolreader.domain.usecase.user

import com.therxmv.dirolreader.domain.models.UserModel
import com.therxmv.dirolreader.domain.repository.UserRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(): UserModel =
        userRepository.getCurrentUser()
}