package com.therxmv.dirolreader.data.source.remote.message

import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.MessageModel
import org.drinkless.td.libcore.telegram.Client

interface MessageSource {
    suspend fun getMessagePhoto(client: Client?, photoId: Int): String

    suspend fun getMessagesByPage(
        client: Client?,
        channelsList: List<List<ChannelModel>>
    ): List<MessageModel>
}