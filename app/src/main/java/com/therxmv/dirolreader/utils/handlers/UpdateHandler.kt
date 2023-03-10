package com.therxmv.dirolreader.utils.handlers

import android.util.Log
import com.therxmv.dirolreader.utils.AuthState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class UpdateHandler: Client.ResultHandler {
    companion object {
        private val _authorizationState = MutableStateFlow<TdApi.AuthorizationState?>(null)
        val authorizationState = _authorizationState.asSharedFlow()
        val chatIds = mutableListOf<Long>()
    }

    override fun onResult(`object`: TdApi.Object?) {
        when(`object`?.constructor) {
            TdApi.UpdateAuthorizationState.CONSTRUCTOR -> {
                _authorizationState.value = (`object` as TdApi.UpdateAuthorizationState).authorizationState
            }
            TdApi.UpdateNewChat.CONSTRUCTOR -> {
                val chat = `object` as TdApi.UpdateNewChat
                if(chat.chat.type is TdApi.ChatTypeSupergroup && (chat.chat.type as TdApi.ChatTypeSupergroup).isChannel) {
                    chatIds.add(chat.chat.id)
//                    Log.d("chatlist123", "${chat.chat.id}: ${chat.chat.title}")
                }
            }
        }
    }
}