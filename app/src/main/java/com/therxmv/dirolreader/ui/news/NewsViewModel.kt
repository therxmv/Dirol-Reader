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
    private val deleteMessageUseCase: DeleteMessageUseCase,
) : ViewModel() {
    private var client: Client? = null

    private val _pagingData = MutableSharedFlow<PagingData<PostModel>>()
    val pagingData = _pagingData.asSharedFlow()

    private val _loadedCount = MutableStateFlow(0)
    val loadedCount = _loadedCount.asStateFlow()

    fun deleteMessage(messageModel: MessageModel) {
        viewModelScope.launch {
            deleteMessageUseCase.invoke(messageModel)
        }
    }

    fun getClient() {
        client = getClientUseCase.invoke(UpdateHandler())
        loadChannelsFromTdLib()
    }

    fun loadChannelsFromTdLib() {
        client?.send(GetChats(ChatListMain(), Int.MAX_VALUE), ChatsResultHandler())

//        viewModelScope.launch {
//            loadedCount.collectLatest {
//                if(it > 5) {
//                    loadMessagesByPage()
//                }
//            }
//        }
    }

    fun loadMessagesByPage() {
        viewModelScope.launch {
            getMessagesByPageUseCase.invoke().collect {
                _pagingData.emit(it)
            }
        }
    }

    private fun saveMessage(chat: Chat, message: Message, isNew: Boolean) {
        when(message.content) {
            is MessageText -> {
                addMessageToRoomUseCase.invoke(
                    MessageModel(
                        message.id,
                        message.messageThreadId,
                        chat.id,
                        message.date,
                        (message.content as MessageText).text.text,
                        null,
                        message.id == chat.lastMessage?.id,
                        isNew
                    )
                )
            }
            else -> {
                addMessageToRoomUseCase.invoke(
                    MessageModel(
                        message.id,
                        message.messageThreadId,
                        chat.id,
                        message.date,
                        "This message type is not yet supported",
                        null,
                        message.id == chat.lastMessage?.id,
                        isNew
                    )
                )
            }
            // TODO handle another types
        }

        _loadedCount.value += 1
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

//                                client?.send(DownloadFile(c.photo?.small?.id!!, 20, 0, 1, false)) {}
                                addChannelToRoomUseCase.invoke(
                                    ChannelModel(
                                        c.id,
                                        c.title,
                                        c.photo?.small?.id,
                                    )
                                )

//                                Log.d("rozmi", c.unreadCount.toString())
                                if(c.unreadCount == 0) {
                                    client?.send(GetMessage(c.id, c.lastMessage?.id!!)) { m ->
                                        m as Message
//                                        Log.d("rozmi", "Old: ${c.title}: ${(m.content as MessageText).text.text}")
                                        saveMessage(c, m, false)
                                    }
                                }
                                else {
                                    client?.send(GetChatHistory(c.id, 0, 0, c.unreadCount, false)) { ms ->
                                        ms as Messages

//                                        Log.d("rozmi", "New: ${c.title}: ${ms.messages.size}")
                                        ms.messages.map { m ->
                                            m as Message
//                                            Log.d("rozmi", "New: ${c.title}: ${(m.content as MessageText).text.text}")
                                            saveMessage(c, m, true)
                                        }
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