package com.therxmv.dirolreader.data.source.remote

import android.util.Log
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.MessageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Chat
import org.drinkless.td.libcore.telegram.TdApi.Message
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MessageRemoteDataSource {
    companion object {
        var lastChannelAndMessageId: Pair<Long, Long>? = null
    }

    suspend fun getMessagePhoto(client: Client?, photoId: Int): String {
        return withContext(Dispatchers.IO) {
            suspendCoroutine {
                client?.send(TdApi.DownloadFile(photoId, 32, 0, 0, true)) { f ->
                    f as TdApi.File
                    it.resume(f.local.path)
                }
            }
        }
    }

    @OptIn(FlowPreview::class)
    suspend fun getMessagesByPage(
        client: Client?,
        channelsList: List<List<ChannelModel>>
    ): List<MessageModel> {
        return withContext(Dispatchers.IO) {
            channelsList.asFlow().map { elem ->
                val channel = elem.last()
                val historyOffset = elem.size + 1

                if(lastChannelAndMessageId == null
                    || lastChannelAndMessageId!!.first != channel.id
                ) {
                    lastChannelAndMessageId = Pair(channel.id, channel.lastReadMessageId)
                }

                suspendCoroutine { cont ->
                    client?.send(
                        TdApi.GetChatHistory(
                            channel.id,
                            lastChannelAndMessageId!!.second,
                            historyOffset * -1,
                            historyOffset,
                            false
                        )
                    ) { ms ->
                        ms as TdApi.Messages
                        val list = ms.messages.reversed().toMutableList().also {
                            if(historyOffset == ms.messages.size) {
                                it.removeAt(0)
                            }
                        }

                        lastChannelAndMessageId = Pair(channel.id, list.last().id)

                        cont.resume(
                            list.asFlow().map { m ->
                                m as Message
                                val model = handleMessageType(channel, m)

                                suspendCoroutine { cont ->
                                    client.send(TdApi.GetChat(channel.id)) { c ->
                                        c as Chat

                                        if (c.photo != null) {
                                            client.send(TdApi.DownloadFile(c.photo?.small?.id!!, 22, 0, 1, true)) { f ->
                                                f as TdApi.File

                                                cont.resume(
                                                    model.copy(
                                                        channelName = c.title,
                                                        channelAvatarPath = f.local.path
                                                    )
                                                )
                                            }
                                        } else {
                                            cont.resume(
                                                model.copy(
                                                    channelName = c.title
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        )
                    }
                }
            }.flattenConcat().toList()
        }
    }

    private fun handleMessageType(
        channel: ChannelModel,
        message: TdApi.Message,
    ): MessageModel {
        val defaultModel = MessageModel(
            message.id,
            channel.id,
            channel.rating,
            "",
            null,
            message.date,
            "This message type is not yet supported",
            null
        )

        return when (message.content) {
            is TdApi.MessageText -> {
                defaultModel.copy(
                    text = (message.content as TdApi.MessageText).text.text,
                )
            }

            is TdApi.MessagePhoto -> {
                defaultModel.copy(
                    text = (message.content as TdApi.MessagePhoto).caption.text,
                    photo = (message.content as TdApi.MessagePhoto).photo.sizes[2]
                )
            }

            is TdApi.MessageVideo -> {
                defaultModel.copy(
                    text = "*VIDEO is not yet supported* ${(message.content as TdApi.MessageVideo).caption.text}",
                )
            }

            is TdApi.MessageDocument -> {
                defaultModel.copy(
                    text = "*FILE is not yet supported* ${(message.content as TdApi.MessageDocument).caption.text}",
                )
            }

            else -> {
                defaultModel
            }
        }
    }
}