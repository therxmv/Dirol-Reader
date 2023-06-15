package com.therxmv.dirolreader.data.source.remote

import android.util.Log
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.MessageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MessageRemoteDataSource {
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

    suspend fun getMessagesByPage(client: Client?, channelsList: List<Pair<ChannelModel, Int>>): Flow<List<MessageModel>> {
        return withContext(Dispatchers.IO) {
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
                                client?.send(TdApi.GetChatHistory(channel.id, channel.lastReadMessageId, historyOffset, historyOffset * -1, false)) { ms ->
                                    ms as TdApi.Messages
                                    val m = ms.messages.first()
//                                    Log.d("rozmi_id", m.id.toString())

                                    client.send(TdApi.GetChat(channel.id)) { c ->
                                        c as TdApi.Chat
//                                        if(c.title == "Dirol test channel") {
//                                            Log.d("rozmi", ms.messages.size.toString())
//                                            ms.messages.forEach { i ->
//                                                Log.d("rozmi", i.content.toString())
//                                            }
//                                        }

                                        if(c.photo != null) {
                                            client.send(TdApi.DownloadFile(c.photo?.small?.id!!, 22, 0, 1, true)) { f ->
                                                f as TdApi.File
                                                handleMessageType(
                                                    continuation,
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
        channel: ChannelModel,
        message: TdApi.Message,
        chat: TdApi.Chat,
        avatarPath: String?
    ) {
        val defaultModel = MessageModel(
            message.id,
            channel.id,
            channel.rating,
            chat.title,
            avatarPath,
            message.date,
            "This message type is not yet supported",
            null
        )

        when(message.content) {
            is TdApi.MessageText -> {
                continuation.resume(
                    defaultModel.copy(
                        text = (message.content as TdApi.MessageText).text.text,
                    )
                )
            }
            is TdApi.MessagePhoto -> {
                continuation.resume(
                    defaultModel.copy(
                        text = (message.content as TdApi.MessagePhoto).caption.text,
                        photo = (message.content as TdApi.MessagePhoto).photo.sizes[2]
                    )
                )
            }
            is TdApi.MessageVideo -> {
                continuation.resume(
                    defaultModel.copy(
                        text = "*VIDEO is not yet supported* ${(message.content as TdApi.MessageVideo).caption.text}",
                    )
                )
            }
            is TdApi.MessageDocument -> {
                continuation.resume(
                    defaultModel.copy(
                        text = "*FILE is not yet supported* ${(message.content as TdApi.MessageDocument).caption.text}",
                    )
                )
            }
            else -> {
                continuation.resume(defaultModel)
            }
        }
    }
}