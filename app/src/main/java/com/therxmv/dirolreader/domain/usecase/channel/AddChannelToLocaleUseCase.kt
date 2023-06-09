package com.therxmv.dirolreader.domain.usecase.channel

import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.repository.ChannelRepository

class AddChannelToLocaleUseCase(
    private val channelRepository: ChannelRepository
) {
    suspend operator fun invoke(channel: ChannelModel) {
        channelRepository.addChannelToLocale(channel)
    }

    suspend operator fun invoke(channel: List<ChannelModel>) {
        channelRepository.addChannelToLocale(channel)
    }
}