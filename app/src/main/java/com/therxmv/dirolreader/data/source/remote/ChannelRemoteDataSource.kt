package com.therxmv.dirolreader.data.source.remote

import com.therxmv.dirolreader.domain.models.ChannelModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi.ChatListMain
import org.drinkless.td.libcore.telegram.TdApi.Chats
import org.drinkless.td.libcore.telegram.TdApi.GetChat
import org.drinkless.td.libcore.telegram.TdApi.GetChats
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class ChannelRemoteDataSource {
    suspend fun getRemoteChannelsIds(client: Client?): List<Long> {
        return withContext(Dispatchers.IO) {
            suspendCoroutine {
                client?.send(GetChats(ChatListMain(), Int.MAX_VALUE)) { c ->
                    c as Chats
                    it.resume(c.chatIds.toList())
                }
            }
        }
    }
}