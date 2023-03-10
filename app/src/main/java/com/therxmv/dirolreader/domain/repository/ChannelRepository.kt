package com.therxmv.dirolreader.domain.repository

import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.NewsPostModel
import kotlinx.coroutines.flow.Flow

interface ChannelRepository {
    fun getChannelsByPage(): Flow<PagingData<ChannelModel>>
    fun addChannel(channelModel: ChannelModel)
}