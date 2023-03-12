package com.therxmv.dirolreader.domain.repository

import androidx.paging.PagingData
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.models.PostModel
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    fun getMessagesByPage(): Flow<PagingData<PostModel>>
    fun addMessage(messageModel: MessageModel)
    suspend fun updateMessage(messageModel: MessageModel)
}