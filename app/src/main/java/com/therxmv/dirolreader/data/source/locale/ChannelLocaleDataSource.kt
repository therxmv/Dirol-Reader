package com.therxmv.dirolreader.data.source.locale

import com.therxmv.dirolreader.data.entity.ChannelEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChannelLocaleDataSource(
    private val dirolDao: DirolDao
) {
    suspend fun getAllChannels(): List<ChannelEntity> = withContext(Dispatchers.IO) {
        dirolDao.getAllChannels()
    }

    suspend fun updateChannelRating(id: Long, num: Int) = withContext(Dispatchers.IO) {
        dirolDao.updateChannelRating(id, num)
    }

    suspend fun addChannel(channelEntity: List<ChannelEntity>) = withContext(Dispatchers.IO) {
        channelEntity.map {
            dirolDao.insertOrUpdateChannel(it)
        }.size
    }
}