package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.usecase.user.GetCurrentUserUseCase
import javax.inject.Inject

data class ProfileViewModelUseCase @Inject constructor(
    val getCurrentUser: GetCurrentUserUseCase,
)
