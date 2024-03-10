package com.therxmv.dirolreader.domain.usecase.message

import com.therxmv.dirolreader.domain.repository.MessageRepository
import org.drinkless.td.libcore.telegram.Client
import javax.inject.Inject

class GetMessagePagingUseCase @Inject constructor(
    private val messageRepository: MessageRepository,
) {
    operator fun invoke(client: Client?) =
        messageRepository.getMessagePaging(client)
}