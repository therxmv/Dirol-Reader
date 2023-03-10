package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.repository.ClientRepository
import org.drinkless.td.libcore.telegram.Client

class GetClientUseCase(private val clientRepository: ClientRepository) {
    fun invoke(updateHandler: Client.ResultHandler): Client? {
        return clientRepository.getClient(updateHandler)
    }
}