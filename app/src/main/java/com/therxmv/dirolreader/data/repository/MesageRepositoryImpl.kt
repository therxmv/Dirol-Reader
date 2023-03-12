package com.therxmv.dirolreader.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.therxmv.dirolreader.data.source.locale.message.MessageLocaleDataSource
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.models.PostModel
import com.therxmv.dirolreader.domain.repository.MessageRepository
import com.therxmv.dirolreader.utils.PAGE_SIZE
import kotlinx.coroutines.flow.Flow

class MessageRepositoryImpl(
    private val messageLocaleDataSource: MessageLocaleDataSource,
): MessageRepository {
    override fun getMessagesByPage(): Flow<PagingData<PostModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                messageLocaleDataSource
            }
        ).flow
    }

    override fun addMessage(messageModel: MessageModel) {
        messageLocaleDataSource.addMessage(messageModel)
    }

    override suspend fun updateMessage(messageModel: MessageModel) {
        messageLocaleDataSource.updateMessage(messageModel)
    }
}