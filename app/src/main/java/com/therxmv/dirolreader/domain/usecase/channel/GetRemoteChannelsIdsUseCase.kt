package com.therxmv.dirolreader.domain.usecase.channel

import com.therxmv.dirolreader.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.Client

class GetRemoteChannelsIdsUseCase(
    private val channelRepository: ChannelRepository
) {
    suspend operator fun invoke(client: Client?): Flow<List<Pair<Long, Int>>> {
        return channelRepository.getRemoteChannelsIds(client)
    }
}