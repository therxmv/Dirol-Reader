package com.therxmv.dirolreader.data.source.local.db

import com.therxmv.dirolreader.data.entity.ChannelEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named

class ChannelLocalDataSource @Inject constructor(
    private val dirolDao: DirolDao,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
) {

    suspend fun getAllChannels(): List<ChannelEntity> = withContext(ioDispatcher) {
        dirolDao.getAllChannels()
    }

    suspend fun updateChannelRating(id: Long, num: Int) = withContext(ioDispatcher) {
        dirolDao.updateChannelRating(id, num)
    }

    suspend fun addChannels(channelEntity: List<ChannelEntity>) = withContext(ioDispatcher) {
        channelEntity.map {
            dirolDao.insertOrUpdateChannel(it)
        }.size
    }
}