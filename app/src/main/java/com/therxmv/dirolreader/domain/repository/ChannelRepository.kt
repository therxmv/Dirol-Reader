package com.therxmv.dirolreader.domain.repository

import androidx.paging.PagingData
import com.therxmv.dirolreader.domain.models.ChannelModel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
//    fun getChannelsByPage(): Flow<PagingData<ChannelModel>>
    fun addChannel(channelModel: ChannelModel)
}