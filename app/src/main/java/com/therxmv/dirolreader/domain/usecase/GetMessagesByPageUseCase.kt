package com.therxmv.dirolreader.domain.usecase

import androidx.paging.PagingData
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.models.PostModel
import com.therxmv.dirolreader.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow

class GetMessagesByPageUseCase(
    private val messageRepository: MessageRepository
) {
    fun invoke(): Flow<PagingData<PostModel>> {
        return messageRepository.getMessagesByPage()
    }
}