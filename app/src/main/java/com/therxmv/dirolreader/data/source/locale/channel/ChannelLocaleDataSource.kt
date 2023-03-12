package com.therxmv.dirolreader.data.source.locale.channel

import com.therxmv.dirolreader.data.source.locale.DirolDao
import com.therxmv.dirolreader.data.source.locale.channel.models.ChannelLocaleMapper
import com.therxmv.dirolreader.domain.models.ChannelModel

class ChannelLocaleDataSource(
    private val dirolDao: DirolDao,
) {
    fun addChannel(channelModel: ChannelModel) {
        dirolDao.insertOrUpdateChannel(ChannelLocaleMapper().mapToEntity(channelModel))
    }
}