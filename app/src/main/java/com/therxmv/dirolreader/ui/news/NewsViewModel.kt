package com.therxmv.dirolreader.ui.news

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.filter
import androidx.paging.flatMap
import androidx.paging.map
import com.therxmv.dirolreader.domain.models.ChannelModel
import com.therxmv.dirolreader.domain.models.NewsPostModel
import com.therxmv.dirolreader.domain.usecase.AddChannelToRoomUseCase
import com.therxmv.dirolreader.domain.usecase.GetChannelsByPageUseCase
import com.therxmv.dirolreader.domain.usecase.GetClientUseCase
import com.therxmv.dirolreader.utils.PAGE_SIZE
import com.therxmv.dirolreader.utils.handlers.UpdateHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.Chat
import org.drinkless.td.libcore.telegram.TdApi.ChatList
import org.drinkless.td.libcore.telegram.TdApi.ChatListMain
import org.drinkless.td.libcore.telegram.TdApi.GetChat
import org.drinkless.td.libcore.telegram.TdApi.Message
import org.drinkless.td.libcore.telegram.TdApi.MessageText
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

    fun getClient() {
        client = getClientUseCase.invoke(UpdateHandler())
        loadChannelsFromTdLib()
    }

    private fun loadChannelsFromTdLib() {
        client?.send(TdApi.GetChats(ChatListMain(), PAGE_SIZE), ChatsResultHandler())
    }

    fun loadChannelsByPage() {
        viewModelScope.launch {
            getChannelsByPageUseCase.invoke().collect {
                _pagingData.emit(it)
            }
        }
    }

    inner class ChatsResultHandler : Client.ResultHandler {
        override fun onResult(`object`: TdApi.Object?) {
            when (`object`!!.constructor) {
                TdApi.Error.CONSTRUCTOR -> Log.d(
                    "chatlist123",
                    "Receive an error for LoadChats: $`object`"
                )
                TdApi.Ok.CONSTRUCTOR -> {}
                TdApi.Chats.CONSTRUCTOR -> {
                    UpdateHandler.chatIds.forEach {
                        addChannelToRoomUseCase.invoke(ChannelModel(it))
//                        Log.d("chatlist1234", "${it}")
                    }

                    loadChannelsByPage()
                }
                else -> {
                    Log.d("chatlist123", (`object`.javaClass.kotlin).toString())
                }
            }
        }
    }
}