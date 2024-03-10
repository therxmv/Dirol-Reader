package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.usecase.client.CreateClientUseCase
import javax.inject.Inject

data class AuthViewModelUseCases @Inject constructor(
    val createClientUseCase: CreateClientUseCase,
    val getTdLibParametersUseCase: GetTdLibParametersUseCase,
)