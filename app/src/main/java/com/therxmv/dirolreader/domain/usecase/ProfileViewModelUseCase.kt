package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.usecase.client.GetClientUseCase
import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserAvatarUseCase
import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserUseCase
import javax.inject.Inject

data class ProfileViewModelUseCase @Inject constructor(
    val getClientUseCase: GetClientUseCase,
    val getCurrentUserUseCase: GetCurrentUserUseCase,
    val getCurrentUserAvatarUseCase: GetCurrentUserAvatarUseCase,
)
