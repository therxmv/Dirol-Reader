package com.therxmv.dirolreader.domain.repository

import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.Client.ResultHandler

interface ClientRepository {
    fun getClient(): Client?
    fun createClient(updateHandler: ResultHandler): Client?
}