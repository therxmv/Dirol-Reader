package com.therxmv.dirolreader.domain.usecase.message

import com.therxmv.dirolreader.domain.repository.MessageRepository
import org.drinkless.td.libcore.telegram.Client

class GetMessagePagingUseCase(
    private val messageRepository: MessageRepository,
) {
    operator fun invoke(client: Client?) =
        messageRepository.getMessagePaging(client)
}