package com.therxmv.dirolreader.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.therxmv.dirolreader.data.source.locale.channel.ChannelLocaleDataSource
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import com.therxmv.dirolreader.utils.PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class ChannelRepositoryImpl(
    private val channelLocaleDataSource: ChannelLocaleDataSource
): ChannelRepository {
    override fun addChannel(channelModel: ChannelModel) {
        channelLocaleDataSource.addChannel(channelModel)
    }
}