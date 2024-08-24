package com.therxmv.dirolreader.domain.usecase

import javax.inject.Inject

data class AuthViewModelUseCases @Inject constructor(
    val getTdLibParameters: GetTdLibParametersUseCase,
)