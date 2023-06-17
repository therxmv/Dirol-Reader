package com.therxmv.dirolreader.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.therxmv.dirolreader.data.entity.toDomain
import com.therxmv.dirolreader.data.source.locale.ChannelLocaleDataSource
import com.therxmv.dirolreader.data.source.remote.MessageRemoteDataSource
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.paging.MessagesPagingSource
import com.therxmv.dirolreader.domain.repository.MessageRepository
import com.therxmv.dirolreader.utils.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client

class MessageRepositoryImpl(
    private val channelLocaleDataSource: ChannelLocaleDataSource,
    private val messageRemoteDataSource: MessageRemoteDataSource,
): MessageRepository {
    override fun getMessagePaging(client: Client?): Flow<PagingData<MessageModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                MessagesPagingSource(client, this)
            }
        ).flow
    }

    override suspend fun getMessagesByPage(client: Client?, offset: Int, limit: Int): List<MessageModel> {
        return messageRemoteDataSource.getMessagesByPage(client, getChannelsList(offset, limit))
    }

    private suspend fun getChannelsList(offset: Int, limit: Int): List<List<ChannelModel>> {
        val list = mutableListOf<ChannelModel>()

        channelLocaleDataSource.getAllChannels().map { elem ->
            for (i in 0 until elem.unreadCount) {
                list.add(elem.toDomain())
            }
        }

        val temp = list.slice(offset until list.size).take(limit)

        return temp.groupBy { it.id }.values.toList()
    }

    override suspend fun getMessagePhoto(client: Client?, photoId: Int): String {
        return messageRemoteDataSource.getMessagePhoto(client, photoId)
    }

}