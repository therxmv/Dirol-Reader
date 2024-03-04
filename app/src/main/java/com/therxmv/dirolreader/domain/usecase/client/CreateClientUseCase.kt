package com.therxmv.dirolreader.domain.usecase.client

import com.therxmv.dirolreader.domain.repository.ClientRepository
import org.drinkless.td.libcore.telegram.Client.ResultHandler

class CreateClientUseCase(private val clientRepository: ClientRepository) {
    operator fun invoke(updateHandler: ResultHandler) =
        clientRepository.createClient(updateHandler)
}