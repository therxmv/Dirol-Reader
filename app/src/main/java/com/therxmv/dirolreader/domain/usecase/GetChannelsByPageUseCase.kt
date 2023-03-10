package com.therxmv.dirolreader.domain.usecase

import androidx.paging.PagingData
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import kotlinx.coroutines.flow.Flow

class GetChannelsByPageUseCase(
    private val channelRepository: ChannelRepository
) {
    fun invoke(): Flow<PagingData<ChannelModel>> {
        return channelRepository.getChannelsByPage()
    }
}