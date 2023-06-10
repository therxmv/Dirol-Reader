package com.therxmv.dirolreader.data.source.remote

import com.therxmv.dirolreader.domain.models.ChannelModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.ChatListFilter
import org.drinkless.td.libcore.telegram.TdApi.ChatListMain
import org.drinkless.td.libcore.telegram.TdApi.Chats
import org.drinkless.td.libcore.telegram.TdApi.GetChat
import org.drinkless.td.libcore.telegram.TdApi.GetChats
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ChannelRemoteDataSource {
    suspend fun getRemoteChannelsIds(client: Client?): Flow<List<Pair<Long, Int>>> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine {
                client?.send(GetChats(ChatListMain(), Int.MAX_VALUE)) { chats ->
                    chats as Chats

                    it.resume(
                        flow {
                            emit(chats.chatIds.toList())
                        }.map {
                            val filtered = it.filter { elem ->
                                suspendCoroutine { continuation ->
                                    client.send(GetChat(elem)) { c ->
                                        c as TdApi.Chat
                                        continuation.resume(c.type is TdApi.ChatTypeSupergroup && (c.type as TdApi.ChatTypeSupergroup).isChannel)
                                    }
                                }
                            }
                            val mapped = filtered.map { elem ->
                                suspendCoroutine { continuation ->
                                    client.send(GetChat(elem)) { c ->
                                        c as TdApi.Chat
                                        continuation.resume(Pair(elem, c.unreadCount))
                                    }
                                }
                            }

                            mapped
                        }
                    )
                }
            }
        }
    }
}