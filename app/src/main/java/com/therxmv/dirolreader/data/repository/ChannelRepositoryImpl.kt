package com.therxmv.dirolreader.data.repository

import com.therxmv.dirolreader.data.source.local.db.ChannelLocalDataSource
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import javax.inject.Inject

class ChannelRepositoryImpl @Inject constructor(
    private val channelLocalDataSource: ChannelLocalDataSource,
) : ChannelRepository {

    override suspend fun updateChannelRating(id: Long, num: Int) {
        channelLocalDataSource.updateChannelRating(id, num)
    }
}