package com.therxmv.dirolreader.domain.usecase.message

import com.therxmv.dirolreader.domain.repository.MessageRepository
import org.drinkless.td.libcore.telegram.Client

class GetMessagePhotoUseCase(
    private val messageRepository: MessageRepository
) {
    suspend operator fun invoke(client: Client?, photoId: Int): String {
        return messageRepository.getMessagePhoto(client, photoId)
    }
}