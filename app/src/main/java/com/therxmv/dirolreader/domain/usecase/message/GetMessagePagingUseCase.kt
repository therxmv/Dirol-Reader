package com.therxmv.dirolreader.domain.usecase.message

import androidx.paging.PagingData
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import org.drinkless.td.libcore.telegram.Client

class GetMessagePagingUseCase(
    private val messageRepository: MessageRepository
) {
    operator fun invoke(client: Client?): Flow<PagingData<MessageModel>> {
        return messageRepository.getMessagePaging(client)
    }
}