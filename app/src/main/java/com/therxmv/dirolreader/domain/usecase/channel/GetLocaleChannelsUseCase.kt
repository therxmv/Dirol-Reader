package com.therxmv.dirolreader.domain.usecase.channel

import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.repository.ChannelRepository

class GetLocaleChannelsUseCase(
    private val channelRepository: ChannelRepository
) {
    suspend operator fun invoke(): List<ChannelModel> {
        return channelRepository.getLocaleChannels()
    }
}