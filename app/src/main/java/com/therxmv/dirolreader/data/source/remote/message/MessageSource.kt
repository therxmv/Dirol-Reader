package com.therxmv.dirolreader.data.source.remote.message

import com.therxmv.dirolreader.data.entity.ChannelEntity
import com.therxmv.dirolreader.domain.models.MessageModel
import kotlinx.coroutines.flow.MutableStateFlow

interface MessageSource {

    fun getUnreadChannelsFlow(): MutableStateFlow<List<ChannelEntity>>
    suspend fun getUnreadMessagesByPage(page: Int): List<MessageModel>
}