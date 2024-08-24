package com.therxmv.dirolreader.domain.usecase.message

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.therxmv.common.Paging
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.paging.MessagesPagingSource
import com.therxmv.dirolreader.domain.repository.MessageRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named

class GetMessagePagingUseCase @Inject constructor(
    private val messagesRepository: MessageRepository,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
) {
    operator fun invoke(): Flow<PagingData<MessageModel>> =
        Pager(
            config = PagingConfig(
                initialLoadSize = Paging.PAGE_SIZE,
                pageSize = Paging.PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                MessagesPagingSource(
                    messageRepository = messagesRepository,
                    ioDispatcher = ioDispatcher,
                )
            }
        ).flow
}