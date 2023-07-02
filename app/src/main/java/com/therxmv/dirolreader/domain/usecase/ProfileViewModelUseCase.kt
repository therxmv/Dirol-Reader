package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.usecase.client.GetClientUseCase
import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserAvatarUseCase
import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserUseCase

data class ProfileViewModelUseCase(
    val getClientUseCase: GetClientUseCase,
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val getCurrentUserAvatarUseCase: GetCurrentUserAvatarUseCase,
)
