package com.therxmv.dirolreader.data.source.remote.channel

import com.therxmv.dirolreader.domain.models.ChannelModel
import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.Client

interface ChannelRemoteSource {
    suspend fun getRemoteChannelsIds(client: Client?): Flow<List<ChannelModel>>
}