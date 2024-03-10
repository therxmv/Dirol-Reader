package com.therxmv.dirolreader.domain.usecase.client

import com.therxmv.dirolreader.domain.repository.ClientRepository
import javax.inject.Inject

class GetClientUseCase @Inject constructor(
    private val clientRepository: ClientRepository
) {
    operator fun invoke() = clientRepository.getClient()
}