package com.therxmv.dirolreader.domain.usecase.channel

import com.therxmv.dirolreader.domain.repository.ChannelRepository
import javax.inject.Inject

class GetLocaleChannelsUseCase @Inject constructor(
    private val channelRepository: ChannelRepository,
) {
    suspend operator fun invoke() = channelRepository.getLocaleChannels()
}