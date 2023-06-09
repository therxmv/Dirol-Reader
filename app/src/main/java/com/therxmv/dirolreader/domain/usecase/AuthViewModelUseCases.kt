package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.usecase.client.CreateClientUseCase

data class AuthViewModelUseCases(
    val createClientUseCase: CreateClientUseCase,
    val getTdLibParametersUseCase: GetTdLibParametersUseCase,
)