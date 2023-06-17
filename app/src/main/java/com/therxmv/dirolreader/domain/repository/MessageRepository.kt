package com.therxmv.dirolreader.domain.repository

import androidx.paging.PagingData
import com.therxmv.dirolreader.domain.models.MessageModel
import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.Client

interface MessageRepository {
    fun getMessagePaging(client: Client?): Flow<PagingData<MessageModel>>
    suspend fun getMessagesByPage(client: Client?, offset: Int, limit: Int): List<MessageModel>
    suspend fun getMessagePhoto(client: Client?, photoId: Int): String
}