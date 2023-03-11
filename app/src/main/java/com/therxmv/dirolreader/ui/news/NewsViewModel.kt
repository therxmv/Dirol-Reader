package com.therxmv.dirolreader.ui.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.usecase.AddChannelToRoomUseCase
import com.therxmv.dirolreader.domain.usecase.GetChannelsByPageUseCase
import com.therxmv.dirolreader.domain.usecase.GetClientUseCase
import com.therxmv.dirolreader.utils.handlers.UpdateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.*
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getClientUseCase: GetClientUseCase,
    private val addChannelToRoomUseCase: AddChannelToRoomUseCase,
    private val getChannelsByPageUseCase: GetChannelsByPageUseCase,
) : ViewModel() {
    private var client: Client? = null

    private val _pagingData = MutableSharedFlow<PagingData<ChannelModel>>()
    val pagingData = _pagingData.asSharedFlow()

    private val _loadedCount = MutableStateFlow(0)
    private val loadedCount = _loadedCount.asStateFlow()

    fun getClient() {
        client = getClientUseCase.invoke(UpdateHandler())
        loadChannelsFromTdLib()
    }

    fun loadChannelsFromTdLib() {
        client?.send(GetChats(ChatListMain(), Int.MAX_VALUE), ChatsResultHandler())

        viewModelScope.launch {
            loadedCount.collectLatest {
                if(it > 5) {
                    loadChannelsByPage()
                }
            }
        }
    }

    private fun loadChannelsByPage() {
        viewModelScope.launch {
            getChannelsByPageUseCase.invoke().collect {
                _pagingData.emit(it)
            }
        }
    }

    inner class ChatsResultHandler : Client.ResultHandler {
        override fun onResult(`object`: Object?) {
            when (`object`!!.constructor) {
                Error.CONSTRUCTOR -> Log.d(
                    "chatlist123",
                    "Receive an error for LoadChats: $`object`"
                )
                Ok.CONSTRUCTOR -> {}
                Chats.CONSTRUCTOR -> {
                    val chats = `object` as Chats
                    chats.chatIds.forEach {
                        client?.send(GetChat(it)) { c ->
                            c as Chat

                            if(c.type is ChatTypeSupergroup && (c.type as ChatTypeSupergroup).isChannel) {
//                                Log.d("rozmi", c.title.toString())
                                val message = c.lastMessage as Message

                                client?.send(ViewMessages(c.id, message.messageThreadId, listOf(message.id).toLongArray(), true)) {}

//                                client?.send(GetChatHistory(c.id, c.lastReadInboxMessageId, 0, c.unreadCount, false)) { ms ->
//                                    ms as Messages
//                                    Log.d("rozmi", ms.messages.size.toString())
//                                }

                                when(message.content) {
                                    is MessageText -> {
                                        addChannelToRoomUseCase.invoke(
                                            ChannelModel(
                                                c.id,
                                                c.photo?.small?.id,
                                                c.title,
                                                (message.content as MessageText).text.text,
                                                message.date,
                                                null,
                                                0,
                                            )
                                        )
                                    }
                                    is MessagePhoto -> {
                                        addChannelToRoomUseCase.invoke(
                                            ChannelModel(
                                                c.id,
                                                c.photo?.small?.id,
                                                c.title,
                                                (message.content as MessagePhoto).caption.text,
                                                message.date,
                                                (message.content as MessagePhoto).photo.sizes[0].photo.id,
                                                0,
                                            )
                                        )
                                    }
                                }

                                _loadedCount.value += 1
                            }
                        }
                    }
                }
                else -> {
                    Log.d("chatlist123", (`object`.javaClass.kotlin).toString())
                }
            }
        }
    }
}

/*
var channelModel = ChannelModel(id = chat.chat.id, channelName = chat.chat.title)

                val avatar = chat.chat.photo?.small
                if(avatar?.id != null) {
                    if (avatar.local?.isDownloadingCompleted == true) {
                        channelModel = channelModel.copy(avatarPath = avatar.local.path)
                    }
                    else {
                        client?.send(DownloadFile(avatar.id, 32, 0, 1, true)) { f ->
                            f as TdApi.File
                            channelModel = channelModel.copy(avatarPath = f.local.path)
                        }
                    }
                }

                client?.send(GetMessage(chat.chat.id, chat.chat.lastMessage?.id!!)) { m ->
                    m as TdApi.Message

                    channelModel = channelModel.copy(lastMessageDate = m.date)

                    when(m.content) {
                        is MessageText -> {
                            channelModel = channelModel.copy(lastMessageText = (m.content as MessageText).text.text)
                        }
                        is MessagePhoto -> {
                            channelModel = channelModel.copy(lastMessageText = (m.content as MessagePhoto).caption.text)

                            val photo = (m.content as MessagePhoto).photo.sizes[0].photo
                            if(photo.local.isDownloadingCompleted) {
                                channelModel = channelModel.copy(photosPaths = listOf(photo.local.path))
                            }
                            else {
                                client.send(DownloadFile(photo.id, 32, 0, 1, true)) { f ->
                                    f as TdApi.File
                                    channelModel = channelModel.copy(photosPaths = listOf(f.local.path))
                                }
                            }
                        }
                    }
                }
 */