package com.therxmv.dirolreader.data.source.remote.message

import android.util.Log
import com.therxmv.dirolreader.data.entity.ChannelEntity
import com.therxmv.dirolreader.data.entity.toDomain
import com.therxmv.dirolreader.data.source.remote.channel.ChannelRemoteSource
import com.therxmv.dirolreader.domain.models.ChannelData
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.MessageModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.Chat
import org.drinkless.tdlib.TdApi.Message
import org.drinkless.tdlib.TdApi.Messages
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MessageRemoteDataSource @Inject constructor(
    private val client: Client,
    private val channelRemoteDataSource: ChannelRemoteSource,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
) : MessageSource {

    /**
     * Used to remember last loaded message of current channel.
     * Because TdApi doesn't provide good offset to get messages properly for pagination
     */
    private var lastMessage: MessageModel? = null

    private var allUnreadChannelsFlow: MutableStateFlow<List<ChannelEntity>> = MutableStateFlow(emptyList())
    private var pagedChannels: Map<Int, List<ChannelEntity>> = emptyMap()

    override fun getUnreadChannelsFlow() = allUnreadChannelsFlow

    private suspend fun refreshData() {
        delay(500) // TODO 3 doesn't load actual channel data
        val list = channelRemoteDataSource.getChannelsForPaging()

        lastMessage = null
        allUnreadChannelsFlow.update { list }
        pagedChannels = list.sortMessagesByPage()
    }

    override suspend fun getUnreadMessagesByPage(page: Int): List<MessageModel> =
        withContext(ioDispatcher) {
            // fetches new data about channels on start and refresh
            if (page == 0 || allUnreadChannelsFlow.value.isEmpty() || pagedChannels.isEmpty()) {
                refreshData()
            }

            val channels = pagedChannels[page] ?: return@withContext emptyList()
            Log.d("rozmi", "pages: ${pagedChannels.size} page: $page - $channels")

            val messages = channels.map {
                async {
                    val channel = it.toDomain()
                    val channelData = getChannelData(channel)
                    val history = getChannelHistory(channel)

                    history.map { message ->
                        handleMessageType(channelData, message)
                    }
                }
            }.awaitAll()

            return@withContext messages
                .flatten()
                .saveLastAndDrop(page)
                .groupMediaMessagesInOne()
        }

    private suspend fun getChannelHistory(channel: ChannelModel): List<Message> =
        suspendCoroutine {
            val unreadCount = channel.unreadCount

            val request = if (unreadCount == 1) { // Better to use 0 for one unread message
                TdApi.GetChatHistory(
                    /* chatId = */ channel.id,
                    /* fromMessageId = */ 0,
                    /* offset = */ 0,
                    /* limit = */ 1,
                    /* onlyLocal = */ false,
                )
            } else {
                val limit = channel.unreadCount + 1 // Plus one, because we have last READ messageId
                val localId = lastMessage?.id
                    ?.takeIf { lastMessage?.channelData?.id == channel.id }

                val messageId = localId ?: channel.lastReadMessageId
                val offset = limit * -1

                TdApi.GetChatHistory(
                    /* chatId = */ channel.id,
                    /* fromMessageId = */ messageId,
                    /* offset = */ offset,
                    /* limit = */ limit,
                    /* onlyLocal = */ false,
                )
            }

            client.send(request) { messages ->
                messages as Messages

                val list = messages.messages
                    .reversed()
                    .filter { it.id != channel.lastReadMessageId } // Need to drop last READ message

                it.resume(list)
            }
        }

    private suspend fun getChannelData(channel: ChannelModel): ChannelData =
        suspendCoroutine { continuation ->
            client.send(TdApi.GetChat(channel.id)) { chat ->
                chat as Chat
                val smallPhotoId = chat.photo?.small?.id

                if (smallPhotoId != null) {
                    client.send(
                        TdApi.DownloadFile(
                            /* fileId = */ smallPhotoId,
                            /* priority = */ 32,
                            /* offset = */ 0,
                            /* limit = */ 0,
                            /* synchronous = */ true,
                        )
                    ) { file ->
                        file as TdApi.File

                        continuation.resume(
                            ChannelData(
                                id = channel.id,
                                rating = channel.rating,
                                name = chat.title,
                                avatarPath = file.local.path
                            )
                        )
                    }
                } else {
                    continuation.resume(
                        ChannelData(
                            id = channel.id,
                            rating = channel.rating,
                            name = chat.title
                        )
                    )
                }
            }
        }

    private fun List<MessageModel>.saveLastAndDrop(page: Int) = this.run {
        val last = lastOrNull()
        val nextPageChannelId = pagedChannels[page + 1]?.firstOrNull()?.id

        if (last?.channelData?.id == nextPageChannelId) {
            lastMessage = last
            dropLast(1)
        } else {
            lastMessage = null
            this
        }
    }
}