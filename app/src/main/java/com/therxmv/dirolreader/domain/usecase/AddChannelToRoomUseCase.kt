package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.repository.ChannelRepository

class AddChannelToRoomUseCase(
    private val channelRepository: ChannelRepository
) {
    fun invoke(channelModel: ChannelModel) {
        channelRepository.addChannel(channelModel)
    }
}