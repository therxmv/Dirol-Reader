package com.therxmv.dirolreader.data.source.locale

import com.therxmv.dirolreader.data.entity.ChannelEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChannelLocaleDataSource(
    private val dirolDao: DirolDao
) {
    suspend fun getAllChannels(): List<ChannelEntity> {
        return withContext(Dispatchers.IO) {
            dirolDao.getAllChannels()
        }
    }

    suspend fun addChannel(channelEntity: ChannelEntity) = withContext(Dispatchers.IO) {
        dirolDao.addChannel(channelEntity)
    }

    suspend fun addChannel(channelEntity: List<ChannelEntity>) = withContext(Dispatchers.IO) {
        dirolDao.addChannels(channelEntity)
    }
}