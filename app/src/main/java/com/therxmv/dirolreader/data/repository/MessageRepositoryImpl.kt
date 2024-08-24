package com.therxmv.dirolreader.data.repository

import com.therxmv.dirolreader.data.entity.toDomain
import com.therxmv.dirolreader.data.source.remote.media.MediaSource
import com.therxmv.dirolreader.data.source.remote.message.MessageSource
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val messageRemoteDataSource: MessageSource,
    private val mediaRemoteDataSource: MediaSource,
) : MessageRepository {

    override suspend fun getUnreadMessagesByPage(page: Int): List<MessageModel> =
        messageRemoteDataSource.getUnreadMessagesByPage(page)

    override suspend fun downloadMediaAndGetPath(mediaId: Int) =
        mediaRemoteDataSource.downloadMediaAndGetPath(mediaId)

    override fun getUnreadChannelsFlow(): Flow<List<ChannelModel>> =
        messageRemoteDataSource.getUnreadChannelsFlow().map { list ->
            list.map { it.toDomain() }
        }
}