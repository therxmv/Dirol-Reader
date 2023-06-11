package com.therxmv.dirolreader.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.therxmv.dirolreader.data.entity.toDomain
import com.therxmv.dirolreader.data.source.locale.ChannelLocaleDataSource
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.paging.MessagesPagingSource
import com.therxmv.dirolreader.domain.repository.MessageRepository
import com.therxmv.dirolreader.utils.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Chat
import org.drinkless.td.libcore.telegram.TdApi.ChatPhotoInfo
import org.drinkless.td.libcore.telegram.TdApi.DownloadFile
import org.drinkless.td.libcore.telegram.TdApi.File
import org.drinkless.td.libcore.telegram.TdApi.GetChat
import org.drinkless.td.libcore.telegram.TdApi.GetChatHistory
import org.drinkless.td.libcore.telegram.TdApi.Message
import org.drinkless.td.libcore.telegram.TdApi.Messages
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MessageRepositoryImpl(
    private val channelLocaleDataSource: ChannelLocaleDataSource
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

    override suspend fun getMessagesByPage(client: Client?, offset: Int, limit: Int): Flow<List<MessageModel>> {
        return withContext(Dispatchers.IO) {
            val channelsList = getChannelsList(offset, limit)

            suspendCoroutine {
                it.resume(
                    flow {
                        emit(channelsList)
                    }.map { list ->
                        list.map { elem ->
                            val channel = elem.first
                            val historyOffset = elem.second - 1

                            suspendCoroutine { continuation ->
                                // TODO add while loop if doesn't load all messages
                                client?.send(GetChatHistory(channel.id, channel.lastReadMessageId, historyOffset, historyOffset * -1, false)) { ms ->
                                    ms as Messages
                                    val m = ms.messages.first()
//                                    Log.d("rozmi_id", m.id.toString())

                                    client.send(GetChat(channel.id)) { c ->
                                        c as Chat
//                                        if(c.title == "Dirol test channel") {
//                                            Log.d("rozmi", ms.messages.size.toString())
//                                            ms.messages.forEach { i ->
//                                                Log.d("rozmi", i.content.toString())
//                                            }
//                                        }

                                        if(c.photo != null) {
                                            client.send(DownloadFile(c.photo?.small?.id!!, 22, 0, 1, true)) { f ->
                                                f as File
                                                handleMessageType(
                                                    continuation,
                                                    client,
                                                    channel,
                                                    m,
                                                    c,
                                                    f.local.path
                                                )
                                            }
                                        }
                                        else {
                                            handleMessageType(
                                                continuation,
                                                client,
                                                channel,
                                                m,
                                                c,
                                                null
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    private fun handleMessageType(
        continuation: Continuation<MessageModel>,
        client: Client?,
        channel: ChannelModel,
        message: Message,
        chat: Chat,
        avatarPath: String?
    ) {
        when(message.content) {
            is TdApi.MessageText -> {
                continuation.resume(
                    MessageModel(
                        message.id,
                        channel.id,
                        channel.rating,
                        chat.title,
                        avatarPath,
                        message.date,
                        (message.content as TdApi.MessageText).text.text,
                        null
                    )
                )
            }
            is TdApi.MessagePhoto -> {
                val photoId = (message.content as TdApi.MessagePhoto).photo.sizes[0].photo.id

                client?.send(DownloadFile(photoId, 32, 0, 1, true)) { f ->
                    f as File

                    continuation.resume(
                        MessageModel(
                            message.id,
                            channel.id,
                            channel.rating,
                            chat.title,
                            avatarPath,
                            message.date,
                            (message.content as TdApi.MessagePhoto).caption.text,
                            f.local.path
                        )
                    )
                }
            }
            else -> {
                continuation.resume(
                    MessageModel(
                        message.id,
                        channel.id,
                        channel.rating,
                        chat.title,
                        avatarPath,
                        message.date,
                        "This message type is not yet supported",
                        null
                    )
                )
            }
        }
    }

    private suspend fun getChannelsList(offset: Int, limit: Int): List<Pair<ChannelModel, Int>> {
        val list = mutableListOf<Pair<ChannelModel, Int>>()

        channelLocaleDataSource.getAllChannels().map { elem ->
            for (i in 0 until elem.unreadCount) {
                list.add(Pair(elem.toDomain(), (i + 1) * -1))
            }
        }

        return list.slice(offset until list.size).take(limit)
    }
}