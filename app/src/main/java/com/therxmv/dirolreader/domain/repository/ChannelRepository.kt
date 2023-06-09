package com.therxmv.dirolreader.domain.repository

import com.therxmv.dirolreader.domain.models.ChannelModel
import org.drinkless.td.libcore.telegram.Client

interface ChannelRepository {
    suspend fun getLocaleChannels(): List<ChannelModel>
    suspend fun addChannelToLocale(channel: ChannelModel)
    suspend fun addChannelToLocale(channel: List<ChannelModel>)
    suspend fun getRemoteChannelsIds(client: Client?): List<Long>
}