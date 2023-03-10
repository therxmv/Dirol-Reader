package com.therxmv.dirolreader.data.repository

import com.therxmv.dirolreader.domain.repository.ClientRepository
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class ClientRepositoryImpl: ClientRepository {
    companion object {
        var client: Client? = null
    }
    override fun getClient(updateHandler: Client.ResultHandler): Client? {
        if(client == null) {
            client = Client.create(updateHandler, null, null)
            client?.send(TdApi.SetLogVerbosityLevel(0)) {}
        }

        return client
    }
}