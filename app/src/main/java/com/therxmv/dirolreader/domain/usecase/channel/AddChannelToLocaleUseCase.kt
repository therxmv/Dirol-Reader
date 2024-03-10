package com.therxmv.dirolreader.domain.usecase.channel

import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.repository.ChannelRepository
import javax.inject.Inject

class AddChannelToLocaleUseCase @Inject constructor(
    private val channelRepository: ChannelRepository,
) {
    suspend operator fun invoke(channel: List<ChannelModel>) =
        channelRepository.addChannelToLocale(channel)
}