package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.repository.MessageRepository

class UpdateMessageUseCase(
    private val messageRepository: MessageRepository
) {
    suspend fun invoke(messageModel: MessageModel) {
        messageRepository.updateMessage(messageModel)
    }
}