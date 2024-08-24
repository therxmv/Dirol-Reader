package com.therxmv.dirolreader.domain.repository

import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.MessageModel
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun getUnreadMessagesByPage(page: Int): List<MessageModel>
    suspend fun downloadMediaAndGetPath(mediaId: Int): String
    fun getUnreadChannelsFlow(): Flow<List<ChannelModel>>
}