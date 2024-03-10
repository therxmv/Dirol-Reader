package com.therxmv.dirolreader.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.therxmv.constants.Paging.PAGE_SIZE
import com.therxmv.dirolreader.data.entity.toDomain
import com.therxmv.dirolreader.data.source.locale.db.ChannelLocaleDataSource
import com.therxmv.dirolreader.data.source.remote.message.MessageSource
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.paging.MessagesPagingSource
import com.therxmv.dirolreader.domain.repository.MessageRepository
import org.drinkless.td.libcore.telegram.Client
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val channelLocaleDataSource: ChannelLocaleDataSource,
    private val messageRemoteDataSource: MessageSource,
) : MessageRepository {

    override fun getMessagePaging(client: Client?) =
        Pager(
            config = PagingConfig(
                initialLoadSize = PAGE_SIZE,
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                MessagesPagingSource(client, this)
            }
        ).flow

    override suspend fun getMessagesByPage(
        client: Client?,
        offset: Int,
        limit: Int
    ) = messageRemoteDataSource.getMessagesByPage(client, getChannelsList(offset, limit))

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

    override suspend fun getMessageMedia(client: Client?, photoId: Int) =
        messageRemoteDataSource.getMessagePhoto(client, photoId)
}