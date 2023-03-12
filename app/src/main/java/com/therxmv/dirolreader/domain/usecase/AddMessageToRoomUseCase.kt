package com.therxmv.dirolreader.domain.usecase

import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.repository.MessageRepository

class AddMessageToRoomUseCase(
    private val messageRepository: MessageRepository
) {
    fun invoke(messageModel: MessageModel) {
        messageRepository.addMessage(messageModel)
    }
}