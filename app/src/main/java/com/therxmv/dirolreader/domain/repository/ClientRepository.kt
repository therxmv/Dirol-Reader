package com.therxmv.dirolreader.domain.repository

import org.drinkless.td.libcore.telegram.Client

interface ClientRepository {
    fun getClient(updateHandler: Client.ResultHandler): Client?
}