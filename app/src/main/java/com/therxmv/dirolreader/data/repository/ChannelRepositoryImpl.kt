package com.therxmv.dirolreader.data.repository

import com.therxmv.dirolreader.data.source.locale.ChannelLocaleDataSource
import com.therxmv.dirolreader.data.entity.toDomain
import com.therxmv.dirolreader.data.source.remote.ChannelRemoteDataSource
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.toEntity
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.Client

class ChannelRepositoryImpl(
    private val channelLocaleDataSource: ChannelLocaleDataSource,
    private val channelRemoteDataSource: ChannelRemoteDataSource
): ChannelRepository {
    override suspend fun getLocaleChannels(): List<ChannelModel> {
        return channelLocaleDataSource.getAllChannels().map { it.toDomain() }
    }

    override suspend fun addChannelToLocale(channel: ChannelModel) {
        channelLocaleDataSource.addChannel(channel.toEntity())
    }

    override suspend fun addChannelToLocale(channel: List<ChannelModel>) {
        channelLocaleDataSource.addChannel(channel.map { it.toEntity() })
    }

    override suspend fun getRemoteChannelsIds(client: Client?): Flow<List<Pair<Long, Int>>> {
        return channelRemoteDataSource.getRemoteChannelsIds(client)
    }
}