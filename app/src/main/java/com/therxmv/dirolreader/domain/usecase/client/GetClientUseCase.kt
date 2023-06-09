package com.therxmv.dirolreader.domain.usecase.client

import com.therxmv.dirolreader.domain.repository.ClientRepository

class GetClientUseCase(private val clientRepository: ClientRepository) {
    operator fun invoke() = clientRepository.getClient()
}