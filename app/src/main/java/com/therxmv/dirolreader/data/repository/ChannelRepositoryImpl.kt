package com.therxmv.dirolreader.data.repository

import com.therxmv.dirolreader.data.entity.toDomain
import com.therxmv.dirolreader.data.source.locale.db.ChannelLocaleDataSource
import com.therxmv.dirolreader.data.source.remote.channel.ChannelRemoteSource
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.toEntity
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import org.drinkless.td.libcore.telegram.Client
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor(
    private val channelLocaleDataSource: ChannelLocaleDataSource,
    private val channelRemoteDataSource: ChannelRemoteSource,
) : ChannelRepository {

    override suspend fun getLocaleChannels() =
        channelLocaleDataSource.getAllChannels().map { it.toDomain() }

    override suspend fun addChannelToLocale(channel: List<ChannelModel>) =
        channelLocaleDataSource.addChannel(channel.map { it.toEntity() })

    override suspend fun getRemoteChannelsIds(client: Client?) =
        channelRemoteDataSource.getRemoteChannelsIds(client)

    override suspend fun updateChannelRating(id: Long, num: Int) {
        channelLocaleDataSource.updateChannelRating(id, num)
    }
}