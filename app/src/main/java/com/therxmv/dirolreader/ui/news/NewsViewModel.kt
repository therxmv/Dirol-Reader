package com.therxmv.dirolreader.ui.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.MessageModel
import com.therxmv.dirolreader.domain.models.PostModel
import com.therxmv.dirolreader.domain.usecase.*
import com.therxmv.dirolreader.utils.handlers.UpdateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi.*
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getClientUseCase: GetClientUseCase,
    private val addChannelToRoomUseCase: AddChannelToRoomUseCase,
    private val addMessageToRoomUseCase: AddMessageToRoomUseCase,
    private val getMessagesByPageUseCase: GetMessagesByPageUseCase,
    private val updateMessageUseCase: UpdateMessageUseCase,
) : ViewModel() {
    private var client: Client? = null

    private val _pagingData = MutableSharedFlow<PagingData<PostModel>>()
    val pagingData = _pagingData.asSharedFlow()

    private val _loadedCount = MutableStateFlow(0)
    private val loadedCount = _loadedCount.asStateFlow()

    fun updateMessage(messageModel: MessageModel) {
        viewModelScope.launch {
            updateMessageUseCase.invoke(messageModel)
        }
    }

    fun getClient() {
        client = getClientUseCase.invoke(UpdateHandler())
        loadChannelsFromTdLib()
    }

    fun loadChannelsFromTdLib() {
        client?.send(GetChats(ChatListMain(), Int.MAX_VALUE), ChatsResultHandler())

        viewModelScope.launch {
            loadedCount.collectLatest {
                if(it > 5) {
                    loadMessagesByPage()
                }
            }
        }
    }

    private fun loadMessagesByPage() {
        viewModelScope.launch {
            getMessagesByPageUseCase.invoke().collect {
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

                                addChannelToRoomUseCase.invoke(
                                    ChannelModel(
                                        c.id,
                                        c.title,
                                        c.photo?.small?.local?.path,
                                    )
                                )
//                                client?.send(DownloadFile(c.photo?.small?.id!!, 20, 0, 1, false)) {}

                                client?.send(GetChatHistory(c.id, c.lastReadInboxMessageId, 0, c.unreadCount, false)) { ms ->
                                    ms as Messages
//                                    Log.d("rozmi", ms.messages.size.toString())

                                    ms.messages.map { m ->
                                        m as Message

                                        when(m.content) {
                                            is MessageText -> {
                                                addMessageToRoomUseCase.invoke(
                                                    MessageModel(
                                                        m.id,
                                                        m.messageThreadId,
                                                        c.id,
                                                        m.date,
                                                        (m.content as MessageText).text.text,
                                                        null,
                                                        false,
                                                        m.id == c.lastMessage?.id
                                                    )
                                                )
                                            }
                                            // TODO handle another types
                                        }

                                        _loadedCount.value += 1
                                    }
                                }
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