package com.therxmv.dirolreader.domain.usecase.message

import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUnreadChannelsFlowUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
) {

    operator fun invoke(): Flow<List<ChannelModel>> =
        messageRepository.getUnreadChannelsFlow()
}