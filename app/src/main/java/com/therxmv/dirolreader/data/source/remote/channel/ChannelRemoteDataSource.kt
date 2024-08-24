package com.therxmv.dirolreader.data.source.remote.channel

import com.therxmv.dirolreader.data.entity.ChannelEntity
import com.therxmv.dirolreader.data.source.local.db.ChannelLocalDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import org.drinkless.tdlib.Client
import org.drinkless.tdlib.TdApi
import org.drinkless.tdlib.TdApi.ChatListMain
import org.drinkless.tdlib.TdApi.Chats
import org.drinkless.tdlib.TdApi.GetChat
import org.drinkless.tdlib.TdApi.GetChats
import javax.inject.Inject
import javax.inject.Named
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ChannelRemoteDataSource @Inject constructor(
    private val client: Client,
    @Named("IO") private val ioDispatcher: CoroutineDispatcher,
    private val channelLocalDataSource: ChannelLocalDataSource,
) : ChannelRemoteSource {

    override suspend fun getChannelsForPaging(): List<ChannelEntity> =
        withContext(ioDispatcher) {
            val localChannels = channelLocalDataSource.getAllChannels()
            val chats = getAllChats()

            val channelList = chats.chatIds.map { id ->
                async {
                    getChannelOrNull(
                        chatId = id,
                        getRating = { id ->
                            localChannels.firstOrNull { it.id == id }?.rating ?: 0
                        }
                    )
                }
            }.awaitAll()

            channelList
                .filterNotNull()
                .also { channelLocalDataSource.addChannels(it) }
                .filter { it.unreadCount > 0 }
                .sortedByDescending { it.rating }
        }

    private suspend fun getAllChats(): Chats =
        suspendCoroutine {
            client.send(GetChats(ChatListMain(), Int.MAX_VALUE)) { chats ->
                chats as Chats
                it.resume(chats)
            }
        }

    private suspend fun getChannelOrNull(chatId: Long, getRating: (Long) -> Int): ChannelEntity? =
        suspendCoroutine { continuation ->
            client.send(GetChat(chatId)) { chat ->
                chat as TdApi.Chat
                val type = chat.type

                val channel = if (type is TdApi.ChatTypeSupergroup && type.isChannel) {
                    ChannelEntity(
                        id = chat.id,
                        unreadCount = chat.unreadCount,
                        lastReadMessageId = chat.lastReadInboxMessageId,
                        rating = getRating(chat.id),
                    )
                } else {
                    null
                }

                continuation.resume(channel)
            }
        }

}